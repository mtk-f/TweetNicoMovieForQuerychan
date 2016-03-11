package jp.fujiu.TweetNicoMovieForQuerychan;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ResponseValue {
	public int _rowid;
	public String channel_id;
	public String cmsid;
	public int comment_counter;
	public String description;
	public String last_res_body;
	public int length_seconds;
	public String main_community_id;
	public int mylist_counter;
	public String start_time;
	public String thumbnail_url;
	public String title;
	public int view_counter;
}
