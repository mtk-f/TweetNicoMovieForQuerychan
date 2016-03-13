package jp.fujiu.TweetNicoMovieForQuerychan;

import java.io.IOException;
import java.util.Random;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringEscapeUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class App {
	public static void main(String[] args) {
		System.out.println("start : " + App.class.getName());

		final String envEnabled = "ApplicationEnabled";

		// 必須環境変数のチェック.
		String[] requiredEnvVariables = {
				envEnabled,
				"twitter4j.oauth.consumerKey",
				"twitter4j.oauth.consumerSecret",
				"twitter4j.oauth.accessToken",
				"twitter4j.oauth.accessTokenSecret",
		};

		for (String value : requiredEnvVariables) {
			if (System.getenv(value) == null) {
				System.out.printf("Environment variable %1$s is nothing\n", value);
				return;
			}
		}

		// 環境変数が設定されていたらツイート処理を実行する.
		// (Bluemixにjarファイルをアップロードした直後に実行される処理を環境変数で制御).
		boolean doTweet = System.getenv(envEnabled).equalsIgnoreCase("true");
		if (doTweet == false) {
			System.out.printf("ENV '%1$s' is not 'true', so main tweet will not be posted.\n", envEnabled);
		}
		
		try {
			TweetQuerychanMovie(doTweet);
		} catch (TwitterException e) {
			e.printStackTrace();
			return;
		}
	}

	private static void TweetQuerychanMovie(boolean doTweet) throws TwitterException {
		final String TWITTER_SCREEN_NAME = "@mtk_f";
		final String TWEET_TAG = "#クエリちゃんの動画をランダムにツイートするサービス #自動";
		final String URL = "http://api.search.nicovideo.jp/api/";
		final String QUERY = "mmd クエリちゃん";
		final String postContent = String.format(
				"{\"query\":\"%1$s\",\"service\":[\"video\"],\"search\":[\"title\",\"description\",\"tags\"],\"join\":[\"cmsid\",\"title\",\"description\",\"thumbnail_url\",\"start_time\",\"view_counter\",\"comment_counter\",\"mylist_counter\",\"channel_id\",\"main_community_id\",\"length_seconds\",\"last_res_body\"],\"filters\":[],\"sort_by\":\"start_time\",\"order\":\"desc\",\"from\":0,\"size\":25,\"timeout\":10000,\"issuer\":\"pc\",\"reason\":\"user\"}",
				QUERY);

		// ツイッターAPIのシングルトンを設定する.
		Twitter twitter = TwitterFactory.getSingleton();

		// nicovideo.jpをクエリー検索して結果のJSONを取得する.
		String rawJson;
		Client client = Client.create();
		WebResource webResource = client.resource(URL);
		rawJson = webResource.type(MediaType.APPLICATION_JSON_TYPE).post(String.class, postContent);
		client.getExecutorService().shutdown();

		// 2行目のJSONを抜き出す.
		int cr2ndPosition = -1;
		for (int i = 0; i < 2; i++) {
			cr2ndPosition = rawJson.indexOf("\n", cr2ndPosition) + 1;
			if (cr2ndPosition == 0) {
				System.out.println(rawJson);
				String msg = String.format("%1$s %2$s %3$s", TWITTER_SCREEN_NAME, "JSON \\n error", TWEET_TAG);
				twitter.updateStatus(msg);
				return;
			}
		}

		int cr3drPosition = rawJson.indexOf("\n", cr2ndPosition);
		if (cr3drPosition == -1) {
			System.out.println(rawJson);
			String msg = String.format("%1$s %2$s %3$s", TWITTER_SCREEN_NAME, "JSON \\n error", TWEET_TAG);
			twitter.updateStatus(msg);
			return;
		}

		String json2ndLine = rawJson.substring(cr2ndPosition, cr3drPosition);

		// 検索結果のJSONをパースする.
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonDefinition response = mapper.readValue(json2ndLine, JsonDefinition.class);
			if (response.values != null) {
				// 動画をランダムに選ぶ.
				Random random = new Random();
				int index = random.nextInt(response.values.length);
				JsonDefinitionValue value = response.values[index];

				final int maxLength = 50;
				String title = value.title.length() < maxLength ? value.title
						: value.title.substring(0, maxLength) + "...";

				String msg = String.format(
						"今日の %1$s のおすすめ動画は･･･\n" 
						+ "%2$s\n"
						+ "http://www.nicovideo.jp/watch/%3$s\n" 
						+ "%4$s", 
						QUERY,
						StringEscapeUtils.unescapeHtml4(title), 
						value.cmsid, 
						TWEET_TAG);

				System.out.println(msg);
				
				// 選んだ動画をツイッターに投稿する.
				if (doTweet) {
					twitter.updateStatus(msg);
				}
			} else {
				String msg = String.format("%1$s %2$s %3$s", TWITTER_SCREEN_NAME, "response.values was null",
						TWEET_TAG);
				twitter.updateStatus(msg);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			String msg = String.format("%1$s %2$s %3$s", TWITTER_SCREEN_NAME, e.getMessage(), TWEET_TAG);
			twitter.updateStatus(msg);
			return;
		}
	}
}
