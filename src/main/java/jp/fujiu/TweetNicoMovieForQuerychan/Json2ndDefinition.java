package jp.fujiu.TweetNicoMovieForQuerychan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Json2ndDefinition {
	public String dqnid;
	public String type;
	public Json2ndDefinitionValue[] values;
}
