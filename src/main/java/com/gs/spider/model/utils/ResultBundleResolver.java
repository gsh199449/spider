package com.gs.spider.model.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ResultBundleResolver
 *
 * @author Gao Shen
 * @version 16/4/21
 */
public class ResultBundleResolver {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
                try {
                    return sdf.parse(json.getAsJsonPrimitive().getAsString().replaceAll("\"", ""));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }).create();
    private static Logger LOG = LogManager.getLogger(ResultBundleResolver.class);

    /**
     * 解析ResultBundle
     *
     * @param json 服务器返回的json数据
     * @param <T>
     * @return
     */
    public <T> ResultBundle<T> bundle(String json) {
        ResultBundle<T> resultBundle = null;
        try {
            Type objectType = new TypeToken<ResultBundle<T>>() {
            }.getType();
            resultBundle = gson.fromJson(json, objectType);
        } catch (JsonSyntaxException e) {
            LOG.error("无法解析的返回值信息:" + json);
            e.printStackTrace();
        }
        validate(resultBundle);
        return resultBundle;
    }

    /**
     * 解析ResultBundle
     *
     * @param json 服务器返回的json数据
     * @param <T>
     * @return
     */
    public <T> ResultBundle<T> bundle(String json, Type classOfT) {
        ResultBundle<T> resultBundle = null;
        try {
            Type objectType = new ParameterizedType() {
                public Type getRawType() {
                    return ResultBundle.class;
                }

                public Type[] getActualTypeArguments() {
                    return new Type[]{classOfT};
                }

                public Type getOwnerType() {
                    return null;
                }
            };
            resultBundle = gson.fromJson(json, objectType);
        } catch (JsonSyntaxException e) {
            LOG.error("无法解析的返回值信息:" + json);
            e.printStackTrace();
        }
        validate(resultBundle);
        return resultBundle;
    }

    /**
     * 解析ResultListBundle
     *
     * @param json 服务器返回的json数据
     * @param <T>
     * @return
     */
    public <T> ResultListBundle<T> listBundle(String json, Class<T> classOfT) {
        ResultListBundle<T> resultBundle = null;
        try {
            Type objectType = new ParameterizedType() {
                public Type getRawType() {
                    return ResultListBundle.class;
                }

                public Type[] getActualTypeArguments() {
                    return new Type[]{classOfT};
                }

                public Type getOwnerType() {
                    return null;
                }
            };
            resultBundle = gson.fromJson(json, objectType);
        } catch (JsonSyntaxException e) {
            LOG.error("无法解析的返回值信息:" + json);
            e.printStackTrace();
        }
        validate(resultBundle);
        return resultBundle;
    }

    /**
     * 解析ResultListBundle
     *
     * @param json 服务器返回的json数据
     * @param <T>
     * @return
     */
    public <T> ResultListBundle<T> listBundle(String json) {
        ResultListBundle<T> resultBundle = null;
        try {
            Type objectType = new TypeToken<ResultListBundle<T>>() {
            }.getType();
            resultBundle = gson.fromJson(json, objectType);
        } catch (JsonSyntaxException e) {
            LOG.error("无法解析的返回值信息:" + json);
            e.printStackTrace();
        }
        validate(resultBundle);
        return resultBundle;
    }

    private void validate(ResultBundle resultBundle) {
        if (resultBundle == null) {
            LOG.error("返回值为空,请检查参数");
        } else if (!resultBundle.isSuccess()) {
            LOG.error("调用出错,错误信息为:{},追踪编号:{}", resultBundle.getErrorMsg(), resultBundle.getTraceId());
        }
    }
}
