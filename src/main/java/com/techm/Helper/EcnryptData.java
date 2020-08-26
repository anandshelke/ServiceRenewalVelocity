package com.techm.Helper;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import com.TechM.Helper.service_renewal.AesEncryption;
import com.techm.Helper.AesEncryption;

public class EcnryptData {
	
	private static final Log LOG = LogFactory.getLog(EcnryptData.class);
	
	private static int aesKeyLength = 128;
	static byte[] key;
	
	private EcnryptData() {

    }
	
	public static String aesEncryption(String input)
	  {
	HashMap<String, String>requestJson = new HashMap<String, String>();
	
	      try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(aesKeyLength);
			SecretKey ser = keyGen.generateKey();
			
			byte[] iv = new byte[aesKeyLength / 8];
			SecureRandom prng = new SecureRandom();
			prng.nextBytes(iv);
			
			Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			
			aesCipherForEncryption.init(1, ser, new IvParameterSpec(iv));
			
			  byte[] byteDataToEncrypt = input.getBytes();
			  byte[] byteCipherText = aesCipherForEncryption.doFinal(byteDataToEncrypt);
			  key = ser.getEncoded();
			
			  String strCipherText = convertToString(byteCipherText);
			requestJson.put("payloadText", strCipherText);
			requestJson.put("payloadVariable", convertToString(iv));
			requestJson.put("payloadToken", encryptWithPublic(key));
		}
	     catch (Exception e) {
			LOG.error(e);
		} 
	
	    return new Gson().toJson(requestJson);
	  }
	
	  public static PublicKey getPublicKeyFromCert()
	  {
	InputStream ins = (InputStream)EcnryptData.class.getClassLoader().getResourceAsStream("otacert.pem");
	
	PublicKey pk = null;
	    try
	    {
	CertificateFactory f = CertificateFactory.getInstance("X.509");
	X509Certificate certificate = (X509Certificate)f.generateCertificate(ins);
	pk = certificate.getPublicKey();
	    }
	    catch (CertificateException e)
	    {
		LOG.error(e);
	    }
	    return pk;
	  }
	
	  public static String encryptWithPublic(byte[] symKey)
	    throws Exception
	  {
	    byte[] cipherText = null;
	    try
	    {
	PublicKey pub = getPublicKeyFromCert();
	      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	cipher.init(1, pub);
	cipherText = cipher.doFinal(symKey);
	      return convertToString(cipherText);
	    }
	    catch (Exception e)
	    {
		LOG.error(e);
	    }
	    return null;
	  }
	
	  private static String convertToString(byte[] buffer)
	  {
	    return new String(new Base64().encode(buffer));
	  }
	
	  public static String aesDecryption(String response)
	  {
	AesEncryption aesObj = new Gson().fromJson(response, AesEncryption.class);
	    try
	    {
	SecretKey key2 = new SecretKeySpec(key, 0, key.length, "AES");
	
	      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	      byte[] ivBytes = new Base64().decode(aesObj.getPayloadVariable());
	cipher.init(2, key2, new IvParameterSpec(ivBytes));
	      byte[] encryptedTextBytes = new Base64().decode(aesObj.getPayloadText());
	      byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
	      return new String(decryptedTextBytes);
	    }
	    catch (Exception e) {
		LOG.error(e);
	    }
	    return null;
	  }  
	
}
