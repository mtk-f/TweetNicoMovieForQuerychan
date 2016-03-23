# TweetNicoMovieForQuerychan
クエリちゃんの動画をランダムにツイートするサービス

## 概要
nicovideo.jpで投稿日が新しい順に「MMD クエリちゃん」でキーワード検索し、投稿日が新しい順に取得した動画(おそらく最大32件)からランダムに一つ選んでツイッターに投稿します。


## スクリーンショット
![スクリーンショット](https://github.com/mtk-f/TweetNicoMovieForQuerychan/blob/master/TweetNicoMovieForQuerychan.png)

## 開発環境
Eclipse Mars.2 Release

## 使い方
* あらかじめ Twitter の Application Management でキーとトークンを発行する必要があります。
* トークンは Access Level が  Read and write の権限が必要です。

## 実行方法
次の環境変数が必要です。
* Tweetable  
 trueでツイートを投稿します。true以外や未設定ではツイートを投稿しません。
* TwitterApiKey  
Consumer Key (API Key)を設定します。
* TwitterSecretKey  
Consumer Secret (API Secret)を設定します。
* TwitterAccess  
Token=Access Tokeを設定します。
* TwitterAccessSecret  
Access Token Secreを設定します。
* Twitter4jDebug  
trueでTwitter4jのデバッグ機能を有効にします。true以外や未設定ではデバッグ機能を無効にします。


jarファイルに書き出し後、環境変数を設定して実行します。  

Windowsの場合
````bat
SET Tweetable=true
SET TwitterApiKey=Consumer Key (API Key)
SET TwitterSecretKey=Consumer Secret (API Secret)
SET TwitterAccessToken=Access Token
SET TwitterAccessSecret=Access Token Secret
java -jar TweetNicoMovieForQuerychan.jar
````

bash環境の場合
````bash
export Tweetable=true
export TwitterApiKey=Consumer Key (API Key)
export TwitterSecretKey=Consumer Secret (API Secret)
export TwitterAccessToken=Access Token
export TwitterAccessSecret=Access Token Secret
java -jar TweetNicoMovieForQuerychan.jar
````
