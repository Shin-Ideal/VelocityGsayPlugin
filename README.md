# VelocityGsayPlugin
![IMAGE.png](IMAGE.png)
サーバー間で一斉にメッセージを送信することができるVelocity用Pluginです。</br>
一斉送信したメッセージをDiscordに送信することもできます。</br>
また、一斉送信の機能に加えサーバー間でtellを行える機能もあります。</br>

## 導入方法
Velocityのpluginsフォルダに入れて起動してください。</br>
ただしDiscordの連携機能を使う場合、一度サーバーを落としてconfig.ymlを書き換える必要があります。</br>
Discordの連携方法は、下記のように設定することで実装できます。</br>
```
discord:
  enable: true
  token: "botのトークン"
  channel: "送信するテキストチャンネルのid"
```
tokenとchannelは必ずダブルクオーテーション(")で囲むようにしてください。</br>

## 使い方
使い方は至ってシンプルです。普段使っている/sayや/tellのように下記のコマンド使うだけです。</br>
* ```/gsay メッセージ```
  * サーバー全体にメッセージを送信します
  * Discordと連携している場合、指定したテキストチャンネルにも送信されます。
  * ```VelocityGsayPlugin.command.gsay``` をtrueにすると使用できます。
* ```/gtell ユーザー名 メッセージ```
  * 同じプロキシに接続しているプレイヤーにささやくことができます。
  * ```VelocityGsayPlugin.command.gtell``` をtrueにすると使用できます。
