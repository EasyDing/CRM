package com.rm.easy.crm.bean;

/**
 * Created by Easy.D on 2016/11/6.
 */
public class Data {

        private String id;
        private String username;
        private String userpwd;
        private String identity;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public String getUsername() {
            return username;
        }

        public void setUserpwd(String userpwd) {
            this.userpwd = userpwd;
        }
        public String getUserpwd() {
            return userpwd;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }
        public String getIdentity() {
            return identity;
        }
}
