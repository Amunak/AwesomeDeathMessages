package mkremins.fanciful;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013-2014 Max Kreminski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;

/**
 * Represents a JSON string value.
 * Writes by this object will not write name values nor begin/end objects in the JSON stream.
 * All writes merely write the represented string value.
 */
@Immutable
final class JsonString implements JsonRepresentedObject, ConfigurationSerializable {

	private String _value;
	
	public JsonString(String value){
		_value = value;
	}
	
	public void writeJson(JsonWriter writer) throws IOException {
		writer.value(getValue());
	}
	
	public String getValue(){
		return _value;
	}

	public Map<String, Object> serialize() {
		HashMap<String, Object> theSingleValue = new HashMap<String, Object>();
		theSingleValue.put("stringValue", _value);
		return theSingleValue;
	}
	
	public static JsonString deserialize(Map<String, Object> map){
		return new JsonString(map.get("stringValue").toString());
	}
	
	@Override
	public String toString(){
		return _value;
	}
}
