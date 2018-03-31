package pl.nkozera.musclesman.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Generator {

    private String md5;

    public Md5Generator(String string){
        setMd5Code(string);
    }


    private void setMd5Code(String string){

        byte[] bytesOfMessage = new byte[0];
        try {
            bytesOfMessage = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        MessageDigest md;
        byte[] thedigest = null;
        try {
            md = MessageDigest.getInstance("MD5");
            thedigest = md.digest(bytesOfMessage);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            if(thedigest != null) {
                String md5 = new String(thedigest);
                md5 = md5.replaceAll("'","");
                this.md5 = md5;
            }
            else
                throw new NullPointerException();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getMd5Code(){
        return md5;
    }

}
