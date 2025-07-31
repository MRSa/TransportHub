# TransportHub : Intentをデバイスをまたいでやりとりする

-----

## 概要

TransportHubは、共有（Intent.ACTION_SEND）データを一度アプリ内に蓄積し、そのデータを他デバイスにインストールしているTransportHubへ転送することで、デバイス間でのデータ共有を実現します。
モバイルデバイス（スマートフォン）に複数のウェアラブルデバイス（スマートウォッチ）が接続されている場合は、ウェアラブルデバイス間で共有データを転送することも可能です。
JoggingTimerに記録したラップタイムデータをモバイルデバイスに転送する、あるいは他ウェアラブルデバイスに転送する、などを実現するために作成しました。

![TransportHubの概要](https://github.com/MRSa/GokigenOSDN_documents/blob/main/Applications/TransportHub/images/TransportHubSummary.png)

-----

## 機能一覧

* 他アプリからの共有データ（Intentデータ、Intent.ACTION_SEND）の受信と蓄積
* 蓄積した共有データを選択し、他のデバイスにインストールしたTransportHubへ送信
* 蓄積した共有データを Intent.ACTION_SEND で送信

-----

## インストール

Google Play からインストールできます。スマートフォンおよびスマートウォッチ、両方ともにインストールできます。

* [https://play.google.com/store/apps/details?id=net.osdn.ja.gokigen.transporthub](https://play.google.com/store/apps/details?id=net.osdn.ja.gokigen.transporthub)

-----

## 説明

### 画面遷移

以下に画面遷移を示します。他のアプリから受信したデータの一覧を表示する画面と、データの詳細を表示する画面の２つで構成されており、非常にシンプルです。

![画面遷移](https://github.com/MRSa/GokigenOSDN_documents/blob/main/Applications/TransportHub/images/TH-transitions.png)

### 一覧画面

他のアプリから受信したデータの一覧を表示します。データをタッチすると詳細画面に映ります。

![一覧画面](https://github.com/MRSa/GokigenOSDN_documents/blob/main/Applications/TransportHub/images/TH-main.png)

### 詳細画面

選択したデータの詳細を表示します。データを他デバイスや他アプリに送信すること、データの削除ができます。

![詳細画面](https://github.com/MRSa/GokigenOSDN_documents/blob/main/Applications/TransportHub/images/TH-detail.png)

#### アイコンについて

詳細画面に表示するアイコンは、左から「他デバイスへ転送(Transport)」「他アプリと共有(Share)」「データ削除(Delete)」「一覧画面に戻る(Back)」となっています。

![詳細画面のアイコン](https://github.com/MRSa/GokigenOSDN_documents/blob/main/Applications/TransportHub/images/TH-icons.png)

##### 他デバイスへ転送(Transport)

一番左のアイコンを押すと、他のデバイスにインストールしたTransportHubアプリにデータを転送します。

##### 他アプリと共有(Share)

左から二番目のアイコンを押すと、他アプリとの共有を行います。（Intent.ACTION_SEND を送信します。）
受信可能なアプリの一覧が表示されますので、送信先のアプリを選択してください。

##### データ削除(Delete)

ゴミ箱を押すと、表示中のデータを削除します。削除を実行する前に、削除確認のダイアログを表示しますので、本当に削除する場合は「OK」を押してください。

![削除確認ダイアログ](https://github.com/MRSa/GokigenOSDN_documents/blob/main/Applications/TransportHub/images/TH-delete.png)

##### 一覧画面に戻る(Back)

左向き矢印（←）を押すと、一覧画面に戻ります。

### 設定画面(モバイルのみ)

モバイルデバイス用アプリでは、[本ページ](https://github.com/MRSa/TransportHub/Readme.md)や、[プライバシーポリシーのページ](https://github.com/MRSa/GokigenOSDN_documents/blob/main/PrivacyPolicy.md)を表示するためのリンクがある画面があります。

![TransportHubについて](https://github.com/MRSa/GokigenOSDN_documents/blob/main/Applications/TransportHub/images/TH-about.png)

#### 操作説明

操作説明のページ（[本ページ](https://github.com/MRSa/TransportHub/Readme.md)）を開きます。

#### プライバシーポリシー

GOKIGENプロジェクトの[プライバシーポリシーのページ](https://github.com/MRSa/GokigenOSDN_documents/blob/main/PrivacyPolicy.md) を開きます。

-----

## その他

### 制約事項

現時点では、データ転送や削除は手動でのみ可能です。また、転送可能なデータは90kB程度に限定しています。 また添付ファイルなどは取り扱いません。テキストデータのみ取り扱い可能です。添付画像データの取り扱いはできません。

### ソースコード

TransportHubは、オープンソースです。 以下からどうぞ。

* [https://github.com/MRSa/TransportHub.git](https://github.com/MRSa/TransportHub.git)

-----

## 変更履歴

* 1.01m / 1.01w : 初版作成
* 1.02m / 1.02w : 不具合修正
* 1.04m / 1.04w : Android SDK 36に更新

以上
