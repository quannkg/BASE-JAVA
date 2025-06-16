package webapi.infrastructure.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

@Component
@ConfigurationProperties(
        prefix = "pool-tour",
        ignoreUnknownFields = false
)
public class PoolProperties {
    private final Async async = new Async();
    private final Http http = new Http();
    private final Database database = new Database();
    private final Mail mail = new Mail();
    private final Security security = new Security();
    private final ApiDocs apiDocs = new ApiDocs();
    private final Logging logging = new Logging();
    private final CorsConfiguration cors = new CorsConfiguration();
    private final Social social = new Social();
    private final ClientApp clientApp = new ClientApp();
    private final AuditEvents auditEvents = new AuditEvents();

    public PoolProperties() {
    }

    public Async getAsync() {
        return this.async;
    }

    public Http getHttp() {
        return this.http;
    }

    public Database getDatabase() {
        return this.database;
    }


    public Mail getMail() {
        return this.mail;
    }

    public Security getSecurity() {
        return this.security;
    }

    public ApiDocs getApiDocs() {
        return this.apiDocs;
    }

    public Logging getLogging() {
        return this.logging;
    }

    public CorsConfiguration getCors() {
        return this.cors;
    }

    public Social getSocial() {
        return this.social;
    }

    public ClientApp getClientApp() {
        return this.clientApp;
    }

    public AuditEvents getAuditEvents() {
        return this.auditEvents;
    }



    public static class AuditEvents {
        private int retentionPeriod = 30;

        public AuditEvents() {
        }

        public int getRetentionPeriod() {
            return this.retentionPeriod;
        }

        public void setRetentionPeriod(int retentionPeriod) {
            this.retentionPeriod = retentionPeriod;
        }
    }

    public static class ClientApp {
        private String name = "jhipsterApp";

        public ClientApp() {
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public static class Social {
        private String redirectAfterSignIn = "/#/home";

        public Social() {
        }

        public String getRedirectAfterSignIn() {
            return this.redirectAfterSignIn;
        }

        public void setRedirectAfterSignIn(String redirectAfterSignIn) {
            this.redirectAfterSignIn = redirectAfterSignIn;
        }
    }

    public static class Logging {
        private boolean useJsonFormat = false;
        private final Logstash logstash = new Logstash();

        public Logging() {
        }

        public boolean isUseJsonFormat() {
            return this.useJsonFormat;
        }

        public void setUseJsonFormat(boolean useJsonFormat) {
            this.useJsonFormat = useJsonFormat;
        }

        public Logstash getLogstash() {
            return this.logstash;
        }

        public static class Logstash {
            private boolean enabled = false;
            private String host = "localhost";
            private int port = 5000;
            private int ringBufferSize = 512;

            public Logstash() {
            }

            public boolean isEnabled() {
                return this.enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return this.host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return this.port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            /** @deprecated */
            @Deprecated
            public int getQueueSize() {
                return this.getRingBufferSize();
            }

            /** @deprecated */
            @Deprecated
            public void setQueueSize(int queueSize) {
                this.setRingBufferSize(queueSize);
            }

            public int getRingBufferSize() {
                return this.ringBufferSize;
            }

            public void setRingBufferSize(int ringBufferSize) {
                this.ringBufferSize = ringBufferSize;
            }
        }
    }

    public static class ApiDocs {
        private String title = "Application API";
        private String description = "API documentation";
        private String version = "0.0.1";
        private String termsOfServiceUrl;
        private String contactName;
        private String contactUrl;
        private String contactEmail;
        private String license;
        private String licenseUrl;
        private String defaultIncludePattern;
        private String managementIncludePattern;
        private Server[] servers;


        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return this.termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContactName() {
            return this.contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactUrl() {
            return this.contactUrl;
        }

        public void setContactUrl(String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getContactEmail() {
            return this.contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getLicense() {
            return this.license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return this.licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public String getDefaultIncludePattern() {
            return this.defaultIncludePattern;
        }

        public void setDefaultIncludePattern(String defaultIncludePattern) {
            this.defaultIncludePattern = defaultIncludePattern;
        }

        public String getManagementIncludePattern() {
            return this.managementIncludePattern;
        }

        public void setManagementIncludePattern(String managementIncludePattern) {
            this.managementIncludePattern = managementIncludePattern;
        }

        public Server[] getServers() {
            return this.servers;
        }

        public void setServers(Server[] servers) {
            this.servers = servers;
        }

        public static class Server {
            private String url;
            private String description;

            public Server() {
            }

            public String getUrl() {
                return this.url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }

    public static class Security {
        private String allowOrigins;
        private String contentSecurityPolicy = "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:";
        private final ClientAuthorization clientAuthorization = new ClientAuthorization();
        private final Authentication authentication = new Authentication();

        public Security() {
        }

        public ClientAuthorization getClientAuthorization() {
            return this.clientAuthorization;
        }

        public Authentication getAuthentication() {
            return this.authentication;
        }


        public String getContentSecurityPolicy() {
            return this.contentSecurityPolicy;
        }

        public void setContentSecurityPolicy(String contentSecurityPolicy) {
            this.contentSecurityPolicy = contentSecurityPolicy;
        }

        public String getAllowOrigins() {
            return allowOrigins;
        }

        public void setAllowOrigins(String allowOrigins) {
            this.allowOrigins = allowOrigins;
        }

        public static class Authentication {
            private final Jwt jwt = new Jwt();

            public Authentication() {
            }

            public Jwt getJwt() {
                return this.jwt;
            }

            public static class Jwt {
                private String secret;
                private String base64Secret;
                private long tokenValidityInSeconds;
                private long tokenValidityInSecondsForRememberMe;
                private long refreshTokenValidityInSeconds;

                public Jwt() {
                    this.secret = "";
                    this.base64Secret = "";
                    this.tokenValidityInSeconds = 1800L;
                    this.tokenValidityInSecondsForRememberMe = 2592000L;
                    this.refreshTokenValidityInSeconds = 604800L;
                }

                public String getSecret() {
                    return this.secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public String getBase64Secret() {
                    return this.base64Secret;
                }

                public void setBase64Secret(String base64Secret) {
                    this.base64Secret = base64Secret;
                }

                public long getTokenValidityInSeconds() {
                    return this.tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return this.tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }

                public long getRefreshTokenValidityInSeconds() {
                    return this.refreshTokenValidityInSeconds;
                }

                public long setRefreshTokenValidityInSeconds(long refreshTokenValidityInSeconds) {
                    return this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
                }
            }
        }

        public static class ClientAuthorization {
            private String accessTokenUri;
            private String tokenServiceId;
            private String clientId;
            private String clientSecret;

            public ClientAuthorization() {
                this.accessTokenUri = "";
                this.tokenServiceId = "";
                this.clientId = "";
                this.clientSecret = "";
            }

            public String getAccessTokenUri() {
                return this.accessTokenUri;
            }

            public void setAccessTokenUri(String accessTokenUri) {
                this.accessTokenUri = accessTokenUri;
            }

            public String getTokenServiceId() {
                return this.tokenServiceId;
            }

            public void setTokenServiceId(String tokenServiceId) {
                this.tokenServiceId = tokenServiceId;
            }

            public String getClientId() {
                return this.clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return this.clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }
        }
    }

    public static class Mail {
        private boolean enabled = false;
        private String from = "";
        private String baseUrl = "";

        public Mail() {
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getFrom() {
            return this.from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getBaseUrl() {
            return this.baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }


    public static class Database {
        private final Couchbase couchbase = new Couchbase();

        public Database() {
        }

        public Couchbase getCouchbase() {
            return this.couchbase;
        }

        public static class Couchbase {
            private String bucketName;
            private String scopeName;

            public Couchbase() {
            }

            public String getBucketName() {
                return this.bucketName;
            }

            public Couchbase setBucketName(String bucketName) {
                this.bucketName = bucketName;
                return this;
            }

            public String getScopeName() {
                return this.scopeName;
            }

            public Couchbase setScopeName(String scopeName) {
                this.scopeName = scopeName;
                return this;
            }
        }
    }

    public static class Http {
        public Http() {
        }
    }

    public static class Async {
        private int corePoolSize = 2;
        private int maxPoolSize = 50;
        private int queueCapacity = 10000;

        public Async() {
        }

        public int getCorePoolSize() {
            return this.corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return this.maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return this.queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

}
