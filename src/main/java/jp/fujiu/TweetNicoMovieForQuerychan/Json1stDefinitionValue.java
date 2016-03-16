package jp.fujiu.TweetNicoMovieForQuerychan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Json1stDefinitionValue {
    public int _rowid;
    public String service;
    public int total;

}
