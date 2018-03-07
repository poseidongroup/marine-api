/*
 * ReservedCharacters.java
 * Copyright (C) 2018 Raymond Dahlberg
 *
 * This file is part of Java Marine API.
 * <http://ktuukkan.github.io/marine-api/>
 *
 * Java Marine API is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Java Marine API is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java Marine API. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.marineapi.nmea.util;

import java.util.HashMap;

/**
 * Utility class for handling text or messages that use reserved characters that need to be replaced when
 * transmitted as NMEA (Table 3). Example: NAVTEX messages in the NRX sentence, if the text contains
 * a carriage return and line feed, it need to be transmitted as ^0D^0A.
 *
 * @author Raymond Dahlberg
 * @see net.sf.marineapi.nmea.sentence.NRXSentence
 */
public class ReservedCharacters {

	private HashMap<Character, String> reservedMap = new HashMap<>();


	public ReservedCharacters() {

		// Add reserved characters as keys, and their encoded string as value
		reservedMap.put((char)0x0D, "^0D");
		reservedMap.put((char)0x0A, "^0A");
		reservedMap.put((char)0x24, "^24");
		reservedMap.put((char)0x2A, "^2A");
		reservedMap.put((char)0x2C, "^2C");
		reservedMap.put((char)0x21, "^21");
		reservedMap.put((char)0x5C, "^5C");
		reservedMap.put((char)0x5E, "^5E");
		reservedMap.put((char)0x7E, "^7E");
		reservedMap.put((char)0x7F, "^7F");

	}

	/**
	 * Encodes text by replacing reserved characters with the encoding specified for each reserved character in
	 * Table 3.
	 * @param textToEncode text that may include reserved characters
	 * @return text that have the reserved characters replaced, for example \r\n is instead ^0D^0A
	 */
	public String getEncodedText(String textToEncode) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < textToEncode.length(); i++) {
			char c = textToEncode.charAt(i);
			if (reservedMap.containsKey(c)) {
				stringBuilder.append(reservedMap.get(c));
			}
			else {
				stringBuilder.append(c);
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * Decodes text by replacing encoded characters with the original character.
	 * @param textToDecode text that may include encoded characters
	 * @return text that have the encoded characters replaced, for example ^0D^0A is instead \r\n
	 */
	public String getDecodedText(String textToDecode) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < textToDecode.length(); i++) {
			char c = textToDecode.charAt(i);
			if (c == '^') {
				stringBuilder.append((char)Integer.parseInt(textToDecode.substring(i + 1, i + 3), 16));
				i += 2;
			}
			else {
				stringBuilder.append(c);
			}
		}
		return stringBuilder.toString();
	}
}
