package com.palscash.common.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hashing {

	private static final Logger log = LoggerFactory.getLogger( Hashing.class );

	private static final String WHIRLPOOL = "Whirlpool";

	private static final String RIPEMD160 = "RIPEMD160";

	static {
		Security.addProvider( new BouncyCastleProvider() );
	}

	public static byte [ ] whirlpool ( byte [ ] data ) {
		return hash( data, WHIRLPOOL );
	}

	public static byte [ ] ripemd160 ( byte [ ] data ) {
		return hash( data, RIPEMD160 );
	}

	public static byte [ ] whirlpool ( String data ) {
		return hash( StringUtils.getBytesUtf8( data ), WHIRLPOOL );
	}

	public static byte [ ] ripemd160 ( String data ) {
		return hash( StringUtils.getBytesUtf8( data ), RIPEMD160 );
	}

	private static byte [ ] hash ( byte [ ] v, String hashingAlgoName ) {

		try {
			final MessageDigest messageDigest = MessageDigest.getInstance( hashingAlgoName, BouncyCastleProvider.PROVIDER_NAME );
			final byte [ ] hash = messageDigest.digest( v );
			return hash;
		} catch ( NoSuchAlgorithmException e ) {
			log.error( "Error: ", e );
		} catch ( NoSuchProviderException e ) {
			log.error( "Error: ", e );
		}
		return null;
	}

	public static byte [ ] hashPublicKey ( byte [ ] pub ) {

		pub = DigestUtils.sha256( pub );

		pub = DigestUtils.sha256( pub );

		pub = ripemd160( pub );

		return pub;

	}

}
