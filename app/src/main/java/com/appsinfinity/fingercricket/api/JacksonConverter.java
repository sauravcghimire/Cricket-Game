package com.appsinfinity.fingercricket.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * A {@link Converter} which uses Jackson for reading and writing entities.
 *
 * @author Kai Waldron (kaiwaldron@gmail.com)
 */
public class JacksonConverter implements Converter {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final ObjectMapper objectMapper;
    private String charset;

    public JacksonConverter() {
        this(new ObjectMapper());
    }

    public JacksonConverter(ObjectMapper objectMapper) {
        if (objectMapper == null) throw new NullPointerException("objectMapper == null");
        this.objectMapper = objectMapper;
        this.charset = "UTF-8";
    }

    /**
     * Convert an HTTP response body to a concrete object of the specified type.
     *
     * @param body HTTP response body.
     * @param type Target object type.
     * @return Instance of {@code type} which will be cast by the caller.
     * @throws ConversionException if conversion was unable to complete. This will trigger a call to
     *                                                {@link retrofit.Callback#failure(retrofit.RetrofitError)} or throw a
     *                                                {@link retrofit.RetrofitError}. The exception message should report all necessary information
     *                                                about its cause as the response body will be set to {@code null}.
     */
    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
           /* String value = objectMapper.readValue(body.in(), String.class);
            value.isEmpty();
            body.in().close();
            Log.e("Response", value);*/
            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            return objectMapper.readValue(body.in(), javaType);
        } catch (JsonMappingException e) {
            throw new ConversionException(e);
        } catch (JsonParseException e) {
            throw new ConversionException(e);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    /**
     * Convert an object to an appropriate representation for HTTP transport.
     *
     * @param object Object instance to convert.
     * @return Representation of the specified object as bytes.
     */
    @Override
    public TypedOutput toBody(Object object) {
        try {
            return new JsonTypedOutput(objectMapper.writeValueAsBytes(object), charset);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }


    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }

        @Override
        public String fileName() {
            return null;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return jsonBytes.length;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }
}