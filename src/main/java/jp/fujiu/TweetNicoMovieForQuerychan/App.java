package jp.fujiu.TweetNicoMovieForQuerychan;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Random;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringEscapeUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class App {
	// 環境変数.
	private static final boolean IS_ENABLED = "true".equalsIgnoreCase(System.getenv("ApplicationEnabled"));
	private static final boolean TWITTER4J_DEBUG = "true".equalsIgnoreCase(System.getenv("Twitter4jDebug"));
	private static final String TWITTER_API_KEY = System.getenv("TwitterApiKey");
	private static final String TWITTER_SECRET_KEY = System.getenv("TwitterSecretKey");
	private static final String TWITTER_ACCESS_TOKEN = System.getenv("TwitterAccessToken");
	private static final String TWITTER_ACCESS_SECRET = System.getenv("TwitterAccessSecret");
	
	public static void main(String[] args) {
		System.out.println("start " + App.class.getName());

		// 必須環境変数のチェック.
		String[] requiredEnvVariables = {
				TWITTER_API_KEY,
				TWITTER_SECRET_KEY,
				TWITTER_ACCESS_TOKEN,
				TWITTER_ACCESS_SECRET,
		};

		for (String envVariable : requiredEnvVariables) {
			if (envVariable == null) {
				System.out.printf("Environment variables \"TwitterApiKey\", \"TwitterSecretKey\", \"TwitterAccessToken\", \"TwitterAccessSecret\" are required\n", envVariable);
				return;
			}
		}

		// 環境変数の設定によってツイートしないことをログに出力する.
		if (IS_ENABLED == false) {
			System.out.printf("ENV \"ApplicationEnabled\" is not \"true\", so main tweet will not be posted.\n");
		}
		
		try {
			TweetQuerychanMovie();
		} catch (TwitterException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("fin " + App.class.getName());
	}

	private static void TweetQuerychanMovie() throws TwitterException {
		final String TWITTER_SCREEN_NAME = "@mtk_f";
		final String TWEET_TAG = "#クエリちゃんの動画をランダムにツイートするサービス #自動";
		final String URL = "http://api.search.nicovideo.jp/api/";
		final String QUERY = "mmd クエリちゃん";
		final String postContent = String.format(
				"{\"query\":\"%1$s\",\"service\":[\"video\"],\"search\":[\"title\",\"description\",\"tags\"],\"join\":[\"cmsid\",\"title\",\"description\",\"thumbnail_url\",\"start_time\",\"view_counter\",\"comment_counter\",\"mylist_counter\",\"channel_id\",\"main_community_id\",\"length_seconds\",\"last_res_body\"],\"filters\":[],\"sort_by\":\"start_time\",\"order\":\"desc\",\"from\":0,\"size\":%2$d,\"timeout\":10000,\"issuer\":\"pc\",\"reason\":\"user\"}",
				QUERY, 100);

		// ツイッターAPIのインスタンス.
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(TWITTER4J_DEBUG)
		  .setOAuthConsumerKey(TWITTER_API_KEY)
		  .setOAuthConsumerSecret(TWITTER_SECRET_KEY)
		  .setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
		  .setOAuthAccessTokenSecret(TWITTER_ACCESS_SECRET);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

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

		String rawJson2ndLine = rawJson.substring(cr2ndPosition, cr3drPosition);

		// 検索結果のJSONをパースする.
		ObjectMapper mapper = new ObjectMapper();
		try {
			// JSONの2行目をパースする.
			Json2ndDefinition json2nd = mapper.readValue(rawJson2ndLine, Json2ndDefinition.class);
			
			if (json2nd.values == null) {
				String msg = String.format("%1$s %2$s %3$s", TWITTER_SCREEN_NAME, "response.json2nd.values was null",
						TWEET_TAG);
				twitter.updateStatus(msg);
				return;
			}
			
			// 動画をランダムに決める.
			int total = json2nd.values.length;
			Random random = new Random();
			int index = random.nextInt(total);
			
			// 動画を選ぶ.
			Json2ndDefinitionValue value = json2nd.values[index];

			// 長すぎるタイトルを省略する.
			final int maxLength = 40;
			String title = Normalizer.normalize(value.title, Normalizer.Form.NFC).length() 
					< maxLength ? value.title : value.title.substring(0, maxLength) + "...";

			String msg = String.format(
					"今日の %1$s のおすすめ動画は･･･\n" 
					+ "%2$s\n"
					+ "http://www.nicovideo.jp/watch/%3$s\n" 
					+ "最新%4$d件から %5$s", 
					QUERY,
					StringEscapeUtils.unescapeHtml4(title), 
					value.cmsid,
					total, 
					TWEET_TAG);

			System.out.println(msg);
			
			// 選んだ動画をツイッターに投稿する.
			// ただし環境変数 ApplicationEnabled が true のときのみ.
			if (IS_ENABLED) {
				twitter.updateStatus(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
			String msg = String.format("%1$s %2$s %3$s", TWITTER_SCREEN_NAME, e.getMessage(), TWEET_TAG);
			twitter.updateStatus(msg);
			return;
		}
	}
}
