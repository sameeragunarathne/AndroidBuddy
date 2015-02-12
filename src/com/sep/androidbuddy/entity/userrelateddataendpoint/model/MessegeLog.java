/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-07-22 21:53:01 UTC)
 * on 2014-09-18 at 15:48:00 UTC 
 * Modify at your own risk.
 */

package com.sep.androidbuddy.entity.userrelateddataendpoint.model;

/**
 * Model definition for MessegeLog.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the userrelateddataendpoint. For a detailed explanation
 * see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MessegeLog extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Key key;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String message;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String number;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String sender;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String time;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String type;

  /**
   * @return value or {@code null} for none
   */
  public Key getKey() {
    return key;
  }

  /**
   * @param key key or {@code null} for none
   */
  public MessegeLog setKey(Key key) {
    this.key = key;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getMessage() {
    return message;
  }

  /**
   * @param message message or {@code null} for none
   */
  public MessegeLog setMessage(java.lang.String message) {
    this.message = message;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getNumber() {
    return number;
  }

  /**
   * @param number number or {@code null} for none
   */
  public MessegeLog setNumber(java.lang.String number) {
    this.number = number;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getSender() {
    return sender;
  }

  /**
   * @param sender sender or {@code null} for none
   */
  public MessegeLog setSender(java.lang.String sender) {
    this.sender = sender;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getTime() {
    return time;
  }

  /**
   * @param time time or {@code null} for none
   */
  public MessegeLog setTime(java.lang.String time) {
    this.time = time;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getType() {
    return type;
  }

  /**
   * @param type type or {@code null} for none
   */
  public MessegeLog setType(java.lang.String type) {
    this.type = type;
    return this;
  }

  @Override
  public MessegeLog set(String fieldName, Object value) {
    return (MessegeLog) super.set(fieldName, value);
  }

  @Override
  public MessegeLog clone() {
    return (MessegeLog) super.clone();
  }

}
