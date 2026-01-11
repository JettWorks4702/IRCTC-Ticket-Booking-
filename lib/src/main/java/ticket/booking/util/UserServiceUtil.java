package ticket.booking.util;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {

    public static String hashPassword(String plainPass)
    {
        return BCrypt.hashpw(plainPass, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPass, String hashPass)
    {
        return BCrypt.checkpw(plainPass, hashPass);
    }

}
