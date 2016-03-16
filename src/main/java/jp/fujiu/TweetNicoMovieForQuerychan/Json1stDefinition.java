package jp.fujiu.TweetNicoMovieForQuerychan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Json1stDefinition {
    public String dqnid;
    public String type;
    public Json1stDefinitionValue values[];

}
