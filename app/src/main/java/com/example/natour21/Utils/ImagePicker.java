package com.example.natour21.Utils;

import com.example.natour21.R;

public class ImagePicker {

    public static int getImage(String username) {
        if (username != null) {

            if (username.length() == 4 || username.length() == 6) {
                return R.drawable.user1;
            } else if (username.length() == 5 || username.length() == 6) {
                return R.drawable.user2;
            } else if (username.length() == 6 || username.length() == 8) {
                return R.drawable.user3;
            } else if (username.length() == 9 || username.length() == 10) {
                return R.drawable.user4;
            } else if (username.length() == 10 || username.length() == 11) {
                return R.drawable.user5;
            } else if (username.length() == 12 || username.length() == 13) {
                return R.drawable.user6;
            } else if (username.length() == 14 || username.length() == 15) {
                return R.drawable.user7;
            } else if (username.length() == 16 || username.length() == 17) {
                return R.drawable.user8;
            } else if (username.length() == 18 || username.length() == 19) {
                return R.drawable.user9;
            } else if (username.length() == 20 || username.length() == 21) {
                return R.drawable.user10;
            } else if (username.length() >= 22 && username.length() <= 27) {
                return R.drawable.user11;
            } else if (username.length() > 28 && username.length() <= 32) {
                return R.drawable.user12;
            }
            return R.drawable.user1;
        } else {
            return R.drawable.user1;
        }
    }
}
