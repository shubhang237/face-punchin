package com.example.student_attendance_app.Student.Model;

import com.google.gson.annotations.SerializedName;

public class TokenData {
    @SerializedName("type")
    private String type;
    @SerializedName("token")
    private String token;
    @SerializedName("data")
    Userdata data;

    public TokenData(String type, String token, Userdata userData) {
        this.type = type;
        this.token = token;
        this.data = userData;
    }

    public String getType() {
        return type;
    }
    public String getToken() {
        return token;
    }
    public Userdata getUserData() {
        return data;
    }

    public class Userdata {
        Infodata user;
        String rollno;

        public Userdata(Infodata userdata, String rollno) {
            this.user = userdata;
            this.rollno = rollno;
        }

        public Infodata getInfodata() {
            return user;
        }

        public String getRollno() {
            return rollno;
        }

        public class Infodata {
            private String email;
            private String first_name;
            private String last_name;
            public Infodata(String email, String firstName, String lastName) {
                this.email = email;
                this.first_name = firstName;
                this.last_name = lastName;
            }

            public String getEmail() {
                return email;
            }

            public String getFirstName() {
                return first_name;
            }

            public String getLastName() {
                return last_name;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }


}
