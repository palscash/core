package com.palscash.wallet.ui.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConfigurationService {

	private final Logger log = LoggerFactory.getLogger( this.getClass() );

	private Configuration config;

	@Value ( "${config.location}" )
	private String configLocation;

	private File configFile;

	@PostConstruct
	public void init ( ) {

		log.debug( "Config location: " + configLocation );

		configFile = new File( configLocation );

		getConfiguration();
	}

	public Configuration getConfiguration ( ) {

		if ( config == null ) {

			if ( configFile.exists() ) {

				try {

					final String json = FileUtils.readFileToString( configFile, StandardCharsets.UTF_8 );

					config = new ObjectMapper().readValue( json, Configuration.class );

					log.debug( "Loaded config: " + config );
					
				} catch ( IOException e ) {
					log.error( "Error: ", e );
				}
			}

			config = new Configuration();
			this.saveConfiguration();
		}

		return config;
	}

	public void saveConfiguration ( ) {

		try {

			// Json
			String json = new ObjectMapper().writeValueAsString( config );

			// Save to user home location
			FileUtils.writeStringToFile( configFile, json, StandardCharsets.UTF_8 );

		} catch ( Exception e ) {
			log.error( "Error: ", e );
		}

	}

}
