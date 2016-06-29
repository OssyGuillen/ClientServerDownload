import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptPassword {
  public static void main(String[] args) {
    try {
      MD5 clave = new MD5();
      MD5StringUtil string = new MD5StringUtil();
      String result = clave.create("usuario:contraseña");
      String result3 = clave.create("usuario:contraseña");
      System.out.println(result2);
    } catch (NoSuchAlgorithmException ex) {
    }
  }
}

class MD5 {
  public static String create(String content) throws NoSuchAlgorithmException{
    String result = "";
    try {
      byte[] defaultBytes = content.getBytes();
      MessageDigest algorithm = MessageDigest.getInstance("MD5");
      algorithm.reset();
      algorithm.update(defaultBytes);
      byte messageDigest[] = algorithm.digest();

      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++) {
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
      }
      result = hexString.toString();
    } catch (NoSuchAlgorithmException ex) {
    }

    assert !result.isEmpty();
    return result;
  }

}

class MD5StringUtil
{


    public static String md5StringFor(String s)
    {
        try {
          MessageDigest digest = MessageDigest.getInstance("MD5");
          final byte[] hash = digest.digest(s.getBytes());
          final StringBuilder builder = new StringBuilder();
          for (byte b : hash)
          {
              builder.append(Integer.toString(b & 0xFF, 16));    
          }
          return builder.toString();
        } catch (NoSuchAlgorithmException ex) {

        }
        return "";
    }
}