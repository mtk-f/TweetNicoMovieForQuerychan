package jp.fujiu.TweetNicoMovieForQuerychan;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class JsonDefinition {
	public String dqnid;
	public String type;
	public JsonDefinitionValue values[];
}
