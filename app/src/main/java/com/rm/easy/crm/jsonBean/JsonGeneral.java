package com.rm.easy.crm.jsonBean;

import java.util.List;

public class JsonGeneral {

        private String status;
        private String message;
        private List<Data> data;


        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        public List<Data> getData() {
            return data;
        }

}


