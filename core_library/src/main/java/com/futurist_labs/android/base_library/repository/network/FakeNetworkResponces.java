package com.futurist_labs.android.base_library.repository.network;

/**
 * Created by Galeen on 1/2/2018.
 */

public class FakeNetworkResponces {
    public static String getRegisterUserJson() {
        return "{\n" +
                "\"codeId\": 0,\n" +
                "\"email\": \"aa@aa.aaaa\",\n" +
                " \"employer\": \"aaa\",\n" +
                "\"firstName\": \"aaa\",\n" +
                "\"jobTitle\": \"aaa\",\n" +
                " \"lastName\": \"aaa\",\n" +
                " \"locationId\": \"7103f135-c82e-4fc0-a50a-d2f3031a6e6d\",\n" +
                " \"password\": \"aaa\",\n" +
                " \"referralCode\": \"\",\n" +
                " \"status\": \"WAITING_FOR_PROOF\"\n" +
                " }";
    }

    public static String getLoginJson() {
        return "{\n" +
                "  \"expiration\": 3600,\n" +
                "  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTQ4OTAzMDksImNvbmZpcm0iOjE4fQ.HO0O6MEs1OalXDsCDOOxWfn2KhU8ifkPFXiC-aux18U\",\n" +
                "  \"user\": {\n" +
                "    \"company\": \"биби\",\n" +
                "    \"companyEndDate\": null,\n" +
                "    \"companyStartDate\": null,\n" +
                "    \"companyTitle\": null,\n" +
                "    \"date_created\": \"18/01/1970 08:10:22\",\n" +
                "    \"email\": \"2math@mail.bg\",\n" +
                "    \"firstname\": \"Galeen\",\n" +
                "    \"id\": 18,\n" +
                "    \"linked_in\": \"DCX6HF1BNZ\",\n" +
                "    \"login_type\": \"LINKEDIN\",\n" +
                "    \"phone\": \"55554\",\n" +
                "    \"pictureUrl\": \"http://lh3.googleusercontent.com/DbTkpovPKR91yCEL5x8MfkOzNEOlhyDOi6UJzFf2QT4w423_cdzET0FPfX-yeHQ86AvFZscUgkh1fejqXvp22LD_\",\n" +
                "    \"publicProfileUrl\": null,\n" +
                "    \"role\": \"MOBILE\",\n" +
                "    \"subscrFlightNotifications\": false,\n" +
                "    \"subscrNewMessage\": \"CONTACTS_ONLY\",\n" +
                "    \"subscrZatSeatNews\": false,\n" +
                "    \"surname\": \"Gogev\",\n" +
                "    \"timeCreated\": 1498222160,\n" +
                "    \"trips\": \"https://smart-zatsit.appspot.com/api_mobile/v1.0/users/18/trips\",\n" +
                "    \"updated\": 32,\n" +
                "    \"url\": \"https://smart-zatsit.appspot.com/api_mobile/v1.0/users/18\"\n" +
                "  }\n" +
                "}";
    }
}
