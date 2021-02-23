package amaz.objects.TwentyfourSeven.api;

import android.content.Context;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import amaz.objects.TwentyfourSeven.R;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public final class CustomTrust {

    private final OkHttpClient client;
    private final Context context;
    private static final Pattern KEY_PATTERN = Pattern.compile("-{5}BEGIN (?:(RSA|DSA|EC)? )?(ENCRYPTED )?PRIVATE KEY-{5}\\r?\\n([A-Z0-9a-z+/\\r\\n]+={0,2})\\r?\\n-{5}END (?:(?:RSA|DSA|EC)? )?(?:ENCRYPTED )?PRIVATE KEY-{5}\\r?\\n$", Pattern.MULTILINE);

    public CustomTrust(Context context) {

        this.context = context;
        X509TrustManager trustManagerCertificate;
        SSLSocketFactory sslSocketFactory;
        SSLSocket socket;
        try {

            /*String pKeyPassword = "123456";
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream keyInput = trustedKeyInputStream();
            try {
                keyStore.load(keyInput, pKeyPassword.toCharArray());
                keyInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());*/

            trustManagerCertificate = trustManagerForCertificates();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManagerCertificate}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManagerCertificate)
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();

    }

    public OkHttpClient getClient() {
        return client;
    }

    /**
     * Returns an input stream containing one or more certificate PEM files. This implementation just
     * embeds the PEM files in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    private InputStream trustedCertificatesInputStream() {
        return context.getResources().openRawResource(R.raw.app_certificate);
    }

    private InputStream trustedKeyInputStream() {
        return context.getResources().openRawResource(R.raw.app_key);
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     *
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     *
     * <p>See also {@link CertificatePinner}, which can limit trusted certificates while still using
     * the host platform's built-in trust store.
     *
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     *
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private X509TrustManager trustManagerForCertificates() throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(trustedCertificatesInputStream());
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        try {
            final PrivateKey privateKey = loadPrivateKey();
            keyStore.setKeyEntry("key", privateKey, password, certificates.toArray(new Certificate[certificates.size()]));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    protected PrivateKey loadPrivateKey() throws IOException, GeneralSecurityException {
        try (final InputStream is = trustedKeyInputStream()) {
            String password = "";
            final byte[] keyBytes = ByteStreams.toByteArray(is);
            final String keyString = new String(keyBytes, StandardCharsets.US_ASCII);
            final Matcher m = KEY_PATTERN.matcher(keyString);
            byte[] encoded = keyBytes;

            if (m.matches()) {
                /*if (!Strings.isNullOrEmpty(m.group(1))) {
                    throw new IllegalArgumentException("Unsupported key type PKCS#1, please convert to PKCS#8");
                }*/

                encoded = BaseEncoding.base64().decode(m.group(3).replaceAll("[\\r\\n]", ""));
            }

            final EncodedKeySpec keySpec = createKeySpec(encoded, password);
            if (keySpec == null) {
                throw new IllegalArgumentException("Unsupported key type: ");
            }

            final String[] keyAlgorithms = {"RSA", "DSA", "EC"};
            for (String keyAlgorithm : keyAlgorithms) {
                try {
                    @SuppressWarnings("InsecureCryptoUsage") final KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
                    return keyFactory.generatePrivate(keySpec);
                } catch (InvalidKeySpecException e) {
                }
            }

            throw new IllegalArgumentException("Unsupported key type: ");
        }
    }

    private static PKCS8EncodedKeySpec createKeySpec(byte[] keyBytes, String password)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        if (Strings.isNullOrEmpty(password)) {
            return new PKCS8EncodedKeySpec(keyBytes);
        }

        final EncryptedPrivateKeyInfo pkInfo = new EncryptedPrivateKeyInfo(keyBytes);
        final SecretKeyFactory kf = SecretKeyFactory.getInstance(pkInfo.getAlgName());
        final PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        final SecretKey secretKey = kf.generateSecret(keySpec);

        @SuppressWarnings("InsecureCryptoUsage") final Cipher cipher = Cipher.getInstance(pkInfo.getAlgName());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, pkInfo.getAlgParameters());

        return pkInfo.getKeySpec(cipher);
    }

}