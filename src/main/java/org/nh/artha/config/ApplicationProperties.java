package org.nh.artha.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Artha.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = true,ignoreInvalidFields = true)
public class ApplicationProperties {

    private final QuartzScheduler quartzScheduler = new QuartzScheduler();

    private final Staging staging = new Staging();

    private final Configs configs = new Configs();

    private final AthmaBucket athmaBucket = new AthmaBucket();

    public QuartzScheduler getQuartzScheduler() {
        return quartzScheduler;
    }

    public Configs getConfigs() {
        return configs;
    }

    private final ApplicationProperties.Security security = new ApplicationProperties.Security();

    public ApplicationProperties.Security getSecurity() {
        return this.security;
    }

    public AthmaBucket getAthmaBucket() {
        return athmaBucket;
    }

    public Staging getStaging() {
        return staging;
    }

    public static class QuartzScheduler {
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class AthmaBucket {

        private String helpPath;

        private String template;

        private String tempExport;

        public String getStoragePath(String pathReference) {

            switch (pathReference) {
                case "helpPath":
                    return getHelpPath();
                case "tempExport":
                    return getTempExport();
                case "template":
                    return getTemplate();
                default:
                    throw new IllegalArgumentException("Invalid path reference.");
            }
        }

        public String getTempExport() {
            return tempExport;
        }

        public void setTempExport(String tempExport) {
            this.tempExport = tempExport;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getHelpPath() {
            return helpPath;
        }

        public void setHelpPath(String helpPath) {
            this.helpPath = helpPath;
        }
    }

    public static class Configs {
        private Integer indexPageSize;

        public Integer getIndexPageSize() {
            return indexPageSize != null ? indexPageSize : 200;
        }

        public void setIndexPageSize(Integer indexPageSize) {
            this.indexPageSize = indexPageSize;
        }

    }

    public static class Security {

        private final Authentication authentication = new Authentication();

        public Authentication getAuthentication() {
            return authentication;
        }

        public static class Authentication {

            private final Ldap ldap = new Ldap();

            private final Db db = new Db();

            public Ldap getLdap() {
                return ldap;
            }

            public Db getDb() {
                return db;
            }

            public static class Db {

                private boolean configured = Boolean.TRUE;

                public boolean isConfigured() {
                    return configured;
                }

                public void setConfigured(boolean configured) {
                    this.configured = configured;
                }
            }

            public static class Ldap {

                private boolean configured = Boolean.FALSE;

                private String domain;

                private String url;

                private String groupSearchBase;

                private String userSearchBase;

                private String userSearchFilter;

                private String managerDn;

                private String managerPassword;

                private String userDnPattern;

                public String getDomain() {
                    return domain;
                }

                public void setDomain(String domain) {
                    this.domain = domain;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getGroupSearchBase() {
                    return groupSearchBase;
                }

                public void setGroupSearchBase(String groupSearchBase) {
                    this.groupSearchBase = groupSearchBase;
                }

                public String getUserSearchBase() {
                    return userSearchBase;
                }

                public void setUserSearchBase(String userSearchBase) {
                    this.userSearchBase = userSearchBase;
                }

                public String getUserSearchFilter() {
                    return userSearchFilter;
                }

                public void setUserSearchFilter(String userSearchFilter) {
                    this.userSearchFilter = userSearchFilter;
                }

                public String getManagerDn() {
                    return managerDn;
                }

                public void setManagerDn(String managerDn) {
                    this.managerDn = managerDn;
                }

                public String getManagerPassword() {
                    return managerPassword;
                }

                public void setManagerPassword(String managerPassword) {
                    this.managerPassword = managerPassword;
                }

                public boolean isConfigured() {
                    return configured;
                }

                public void setConfigured(boolean configured) {
                    this.configured = configured;
                }

                public void setUserDnPattern(String userDnPattern) {
                    this.userDnPattern = userDnPattern;
                }

                public String getUserDnPattern() {
                    return this.userDnPattern;
                }
            }
        }
    }

    public static class Staging {
        private Mcr mcr= new Mcr();

        private ServiceAnalysis serviceAnalysis = new ServiceAnalysis();

        public Mcr getMcr() {
            return mcr;
        }

        public void setMcr(Mcr mcr) {
            this.mcr = mcr;
        }

        public ServiceAnalysis getServiceAnalysis() {
            return serviceAnalysis;
        }

        public void setServiceAnalysis(ServiceAnalysis serviceAnalysis) {
            this.serviceAnalysis = serviceAnalysis;
        }

        public static class Mcr {
            private String filelocation;

            public String getFilelocation() {
                return filelocation;
            }

            public void setFilelocation(String filelocation) {
                this.filelocation = filelocation;
            }
        }

        public static class ServiceAnalysis {
            private String filelocation;

            public String getFilelocation() {
                return filelocation;
            }

            public void setFilelocation(String filelocation) {
                this.filelocation = filelocation;
            }
        }
    }
}
