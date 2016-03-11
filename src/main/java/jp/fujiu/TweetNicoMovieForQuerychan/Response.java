package jp.fujiu.TweetNicoMovieForQuerychan;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Response {
	public String dqnid;
	public String type;
	public ResponseValue values[];
}
