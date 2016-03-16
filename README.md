# TweetNicoMovieForQuerychan
クエリちゃんの動画をランダムにツイートするサービス

## 概要
nicovideo.jpを「mmd クエリちゃん」でキーワード検索し、該当する動画の一つをランダムに選んでツイートします。

## スクリーンショット
![スクリーンショット](https://github.com/mtk-f/TweetNicoMovieForQuerychan/blob/master/TweetNicoMovieForQuerychan.png)

## TODO
一回の検索で返される動画の数は25件まででのようです。  
25件件を超えた場合の処理は未実装です。

## 開発環境
Version: Mars.2 Release

## 使い方
* あらかじめ Twitter の Application Management でキーとトークンを発行する必要があります。
* トークンは Access Level が  Read and write の権限が必要です。

## 実行方法
次の環境変数が必要です。
* ApplicationEnabled  
 trueでツイートを投稿します。true以外でコンソール出力飲みします。
* TwitterApiKey  
Consumer Key (API Key)を設定します。
* TwitterSecretKey  
Consumer Secret (API Secret)を設定します。
* TwitterAccess  
Token=Access Tokeを設定します。
* TwitterAccessSecret  
Access Token Secreを設定します。


jarファイルに書き出し後、環境変数を設定して実行します。  

Windows場合
````bat
SET ApplicationEnabled=true
SET TwitterApiKey=Consumer Key (API Key)
SET TwitterSecretKey=Consumer Secret (API Secret)
SET TwitterAccessToken=Access Token
SET TwitterAccessSecret=Access Token Secret
java -jar TweetNicoMovieForQuerychan.jar
````

bash環境の場合
````bash
export ApplicationEnabled=true
export TwitterApiKey=Consumer Key (API Key)
export TwitterSecretKey=Consumer Secret (API Secret)
export TwitterAccessToken=Access Token
export TwitterAccessSecret=Access Token Secret
java -jar TweetNicoMovieForQuerychan.jar
````
