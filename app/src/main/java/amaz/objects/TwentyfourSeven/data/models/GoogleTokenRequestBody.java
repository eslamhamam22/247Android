package amaz.objects.TwentyfourSeven.data.models;

public class GoogleTokenRequestBody {

    /*RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id", GOOGLE_CLIENT_ID)
                .add("client_secret", "ujf5yrgNOImmDbH2OzdHtOeD")
                .add("redirect_uri","")
                .add("code", authCode)
                .build();*/

    private String grantType;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String code;

    public GoogleTokenRequestBody(String grantType, String clientId, String clientSecret, String redirectUri, String code) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.code = code;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
