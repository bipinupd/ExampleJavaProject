package com.bu.csvtodb;

import java.nio.file.Paths;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;

public class Configuration {
	private Config config;

	public Configuration(String configLoction) {
		config = ConfigFactory.parseFile(Paths.get(configLoction).toFile(),
				ConfigParseOptions.defaults().setSyntax(ConfigSyntax.CONF));
	}

	public Config getConfig() {
		return config;
	}

}
