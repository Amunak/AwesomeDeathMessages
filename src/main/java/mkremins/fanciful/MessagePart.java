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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;

/**
 * Internal class: Represents a component of a JSON-serializable {@link FancyMessage}.
 */
final class MessagePart implements JsonRepresentedObject, ConfigurationSerializable, Cloneable {

	ChatColor color = ChatColor.WHITE;
	ArrayList<ChatColor> styles = new ArrayList<ChatColor>();
	String clickActionName = null, clickActionData = null,
			hoverActionName = null;
	JsonRepresentedObject hoverActionData = null;
	TextualComponent text = null;

	MessagePart(final TextualComponent text){
		this.text = text;
	}

	MessagePart() {
		this.text = null;
	}

	boolean hasText() {
		return text != null;
	}

        @Override
	@SuppressWarnings("unchecked")
	public MessagePart clone() throws CloneNotSupportedException{
		MessagePart obj = (MessagePart)super.clone();
		obj.styles = (ArrayList<ChatColor>)styles.clone();
		if(hoverActionData instanceof JsonString){
			obj.hoverActionData = new JsonString(((JsonString)hoverActionData).getValue());
		}else if(hoverActionData instanceof FancyMessage){
			obj.hoverActionData = ((FancyMessage)hoverActionData).clone();
		}
		return obj;

	}

	static final BiMap<ChatColor, String> stylesToNames;

	static{
		ImmutableBiMap.Builder<ChatColor, String> builder = ImmutableBiMap.builder();
		for (final ChatColor style : ChatColor.values()){
			if(!style.isFormat()){
				continue;
			}

			String styleName;
			switch (style) {
			case MAGIC:
				styleName = "obfuscated"; break;
			case UNDERLINE:
				styleName = "underlined"; break;
			default:
				styleName = style.name().toLowerCase(); break;
			}
			
			builder.put(style, styleName);
		}
		stylesToNames = builder.build();
	}

	public void writeJson(JsonWriter json) {
		try {
			json.beginObject();
			text.writeJson(json);
			json.name("color").value(color.name().toLowerCase());
			for (final ChatColor style : styles) {
				json.name(stylesToNames.get(style)).value(true);
			}
			if (clickActionName != null && clickActionData != null) {
				json.name("clickEvent")
				.beginObject()
				.name("action").value(clickActionName)
				.name("value").value(clickActionData)
				.endObject();
			}
			if (hoverActionName != null && hoverActionData != null) {
				json.name("hoverEvent")
				.beginObject()
				.name("action").value(hoverActionName)
				.name("value");
				hoverActionData.writeJson(json);
				json.endObject();
			}
			json.endObject();
		} catch(IOException e){
			Bukkit.getLogger().log(Level.WARNING, "A problem occured during writing of JSON string", e);
		}
	}

	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("text", text);
		map.put("styles", styles);
		map.put("color", color.getChar());
		map.put("hoverActionName", hoverActionName);
		map.put("hoverActionData", hoverActionData);
		map.put("clickActionName", clickActionName);
		map.put("clickActionData", clickActionData);
		return map;
	}

	@SuppressWarnings("unchecked")
	public static MessagePart deserialize(Map<String, Object> serialized){
		MessagePart part = new MessagePart((TextualComponent)serialized.get("text"));
		part.styles = (ArrayList<ChatColor>)serialized.get("styles");
		part.color = ChatColor.getByChar(serialized.get("color").toString());
		part.hoverActionName = serialized.get("hoverActionName").toString();
		part.hoverActionData = (JsonRepresentedObject)serialized.get("hoverActionData");
		part.clickActionName = serialized.get("clickActionName").toString();
		part.clickActionData = serialized.get("clickActionData").toString();
		return part;
	}

	static{
		ConfigurationSerialization.registerClass(MessagePart.class);
	}

}
