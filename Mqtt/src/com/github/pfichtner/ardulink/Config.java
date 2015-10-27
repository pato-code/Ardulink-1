/**
Copyright 2013 project Ardulink http://www.ardulink.org/

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */
package com.github.pfichtner.ardulink;

import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

/**
 * [ardulinktitle] [ardulinkversion]
 * 
 * @author Peter Fichtner
 * 
 * [adsense]
 */
public abstract class Config {

	public static class DefaultConfig extends Config {

		private final String topic;
		private Pattern topicPatternDigitalWrite;
		private String topicPatternDigitalRead;
		private Pattern topicPatternAnalogWrite;
		private String topicPatternAnalogRead;
		private Pattern topicPatternDigitalControl;
		private Pattern topicPatternAnalogControl;

		private DefaultConfig(String topic) {
			this.topic = topic;
			this.topicPatternDigitalWrite = compile(write(topic, "D"));
			this.topicPatternDigitalRead = read(topic, "D");
			this.topicPatternAnalogWrite = compile(write(topic, "A"));
			this.topicPatternAnalogRead = read(topic, "A");
		}

		private DefaultConfig(Config c) {
			this.topic = c.getTopic();
			this.topicPatternDigitalWrite = c.getTopicPatternDigitalWrite();
			this.topicPatternDigitalRead = c.getTopicPatternDigitalRead();
			this.topicPatternAnalogWrite = c.getTopicPatternAnalogWrite();
			this.topicPatternAnalogRead = c.getTopicPatternAnalogRead();
			this.topicPatternDigitalControl = c.getTopicPatternDigitalControl();
			this.topicPatternAnalogControl = c.getTopicPatternAnalogControl();
		}

		public String getTopic() {
			return topic;
		}

		public Pattern getTopicPatternDigitalWrite() {
			return topicPatternDigitalWrite;
		}

		public String getTopicPatternDigitalRead() {
			return topicPatternDigitalRead;
		}

		public Pattern getTopicPatternAnalogWrite() {
			return topicPatternAnalogWrite;
		}

		public String getTopicPatternAnalogRead() {
			return topicPatternAnalogRead;
		}

		public Pattern getTopicPatternDigitalControl() {
			return topicPatternDigitalControl;
		}

		public Pattern getTopicPatternAnalogControl() {
			return topicPatternAnalogControl;
		}

		public static DefaultConfig copyOf(Config config) {
			return new DefaultConfig(config);
		}

		public static Config withTopic(String topic) {
			return new DefaultConfig(topic);
		}

	}

	public static final String DEFAULT_TOPIC = "home/devices/ardulink/";

	public static final Config DEFAULT = withTopic(DEFAULT_TOPIC);

	public static Config withTopic(String topic) {
		return DefaultConfig.withTopic(topic);

	}

	public Config withTopicPatternAnalogWrite(Pattern topicPatternAnalogWrite) {
		DefaultConfig copy = DefaultConfig.copyOf(this);
		copy.topicPatternAnalogWrite = topicPatternAnalogWrite;
		return copy;
	}

	public Config withTopicPatternAnalogRead(String topicPatternAnalogRead) {
		DefaultConfig copy = DefaultConfig.copyOf(this);
		copy.topicPatternAnalogRead = topicPatternAnalogRead;
		return copy;
	}

	public Config withTopicPatternDigitalWrite(Pattern topicPatternDigitalWrite) {
		DefaultConfig copy = DefaultConfig.copyOf(this);
		copy.topicPatternDigitalWrite = topicPatternDigitalWrite;
		return copy;
	}

	public Config withTopicPatternDigitalRead(String topicPatternDigitalRead) {
		DefaultConfig copy = DefaultConfig.copyOf(this);
		copy.topicPatternDigitalRead = topicPatternDigitalRead;
		return copy;
	}

	public Config withControlChannelEnabled() {
		DefaultConfig copy = DefaultConfig.copyOf(this);
		copy.topicPatternDigitalControl = compile(write(copy.getTopic()
				+ "system/listening/", "D"));
		copy.topicPatternAnalogControl = compile(write(copy.getTopic()
				+ "system/listening/", "A"));
		return copy;
	}

	private static String read(String brokerTopic, String prefix) {
		return format(brokerTopic, prefix, "%s", "/get");
	}

	private static String write(String brokerTopic, String prefix) {
		return format(brokerTopic, prefix, "(\\w+)", "/set");
	}

	private static String format(String brokerTopic, String prefix,
			String numerated, String appendix) {
		return brokerTopic + prefix + String.format("%s/value", numerated)
				+ appendix;
	}

	protected abstract String getTopic();

	public abstract Pattern getTopicPatternDigitalWrite();

	public abstract Pattern getTopicPatternAnalogWrite();

	public abstract String getTopicPatternDigitalRead();

	public abstract String getTopicPatternAnalogRead();

	public abstract Pattern getTopicPatternDigitalControl();

	public abstract Pattern getTopicPatternAnalogControl();

}
