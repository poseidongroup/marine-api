/*
 * NRXParser.java
 * Copyright (C) 2010 Kimmo Tuukkanen
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
package net.sf.marineapi.nmea.parser;

import net.sf.marineapi.nmea.sentence.NRXSentence;
import net.sf.marineapi.nmea.sentence.SentenceId;
import net.sf.marineapi.nmea.sentence.TalkerId;
import net.sf.marineapi.nmea.util.DataStatus;
import net.sf.marineapi.nmea.util.Date;
import net.sf.marineapi.nmea.util.Time;

import javax.print.attribute.standard.MediaSize;

/**
 * NRX sentence parser.
 *
 * @author Raymond Dahlberg
 */
class NRXParser extends SentenceParser implements NRXSentence {

	// field indices
	private static final int NUMBER_OF_SENTENCES = 0;
	private static final int SENTENCE_NUMBER = 1;
	private static final int SEQUENTIAL_MESSAGE_ID = 2;
	private static final int NAVTEX_MESSAGE_CODE = 3;
	private static final int FREQUENCY_TABLE_INDEX = 4;
	private static final int UTC_OF_RECEIPT_OF_MESSAGE = 5;
	private static final int DAY = 6;
	private static final int MONTH = 7;
	private static final int YEAR = 8;
	private static final int TOTAL_NUMBER_OF_CHARACTERS_IN_THIS_SERIES = 9;
	private static final int TOTAL_NUMBER_OF_BAD_CHARACTERS = 10;
	private static final int DATA_STATUS = 11;
	private static final int MESSAGE_BODY = 12;

	/**
	 * Creates a new instance of NRXParser.
	 *
	 * @param nmea NRX sentence String
	 * @throws IllegalArgumentException If specified sentence is invalid.
	 */
	public NRXParser(String nmea) {
		super(nmea, SentenceId.NRX);
	}

	/**
	 * Creates NRX parser with empty sentence.
	 *
	 * @param talker TalkerId to set
	 */
	public NRXParser(TalkerId talker) {
		super(talker, SentenceId.NRX, 13);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.DateSentence#getDate()
	 */
	public Date getDate() {
		int y = getIntValue(YEAR);
		int m = getIntValue(MONTH);
		int d = getIntValue(DAY);
		return new Date(y, m, d);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.TimeSentence#getTime()
	 */
	public Time getTime() {
		String str = getStringValue(UTC_OF_RECEIPT_OF_MESSAGE);
		return new Time(str);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.marineapi.nmea.sentence.DateSentence#setDate(net.sf.marineapi.
	 * nmea.util.Date)
	 */
	public void setDate(Date date) {
		setIntValue(YEAR, date.getYear());
		setIntValue(MONTH, date.getMonth(), 2);
		setIntValue(DAY, date.getDay(), 2);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.marineapi.nmea.sentence.TimeSentence#setTime(net.sf.marineapi.
	 * nmea.util.Time)
	 */
	public void setTime(Time t) {
		setStringValue(UTC_OF_RECEIPT_OF_MESSAGE, t.toString());
	}


	@Override
	public int getNumberOfSentences() {
		return getIntValue(NUMBER_OF_SENTENCES);
	}

	@Override
	public int getSentenceNumber() {
		return getIntValue(SENTENCE_NUMBER);
	}

	@Override
	public int getSequentialId() {
		return getIntValue(SEQUENTIAL_MESSAGE_ID);
	}

	@Override
	public String getMessageCode() {
		return getStringValue(NAVTEX_MESSAGE_CODE);
	}

	@Override
	public int getFrequencyTableIndex() {
		return getIntValue(FREQUENCY_TABLE_INDEX);
	}

	@Override
	public int getTotalNumberOfCharactersInSeries() {
		return getIntValue(TOTAL_NUMBER_OF_CHARACTERS_IN_THIS_SERIES);
	}

	@Override
	public int getTotalNumberOfBadCharacters() {
		return getIntValue(TOTAL_NUMBER_OF_BAD_CHARACTERS);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.NRXSentence#getDataStatus()
	 */
	@Override
	public DataStatus getStatus() {
		return DataStatus.valueOf(getCharValue(DATA_STATUS));
	}

	@Override
	public String getMessageBody() {
		return getStringValue(MESSAGE_BODY);
	}

	@Override
	public void setNumberOfSentences(int numberOfSentences) {
		setIntValue(NUMBER_OF_SENTENCES, numberOfSentences, 3);
	}

	@Override
	public void setSentenceNumber(int sentenceNumber) {
		setIntValue(SENTENCE_NUMBER, sentenceNumber, 3);
	}

	@Override
	public void setSequentialId(int sequentialId) {
		setIntValue(SEQUENTIAL_MESSAGE_ID, sequentialId, 2);
	}

	@Override
	public void setMessageCode(char transmitterCoverageArea, char subject, int serialNumber) {
		StringBuilder messageCodeBuilder = new StringBuilder();
		messageCodeBuilder.append(transmitterCoverageArea);
		messageCodeBuilder.append(subject);
		if (serialNumber < 0 || serialNumber > 99) {
			throw new IllegalArgumentException("serialNumber need to be from 0 to 99");
		}
		messageCodeBuilder.append(String.format("%02d", serialNumber));
		setStringValue(NAVTEX_MESSAGE_CODE, messageCodeBuilder.toString());
	}

	@Override
	public void setFrequencyTableIndex(int frequencyTableIndex) {
		if (frequencyTableIndex < 0 || frequencyTableIndex > 9) {
			throw new IllegalArgumentException("Frequency table need to be from 0 to 9");
		}
		setIntValue(FREQUENCY_TABLE_INDEX, frequencyTableIndex);
	}

	@Override
	public void setTotalNumberOfCharactersInSeries(int totalNumberOfCharactersInSeries) {
		setIntValue(TOTAL_NUMBER_OF_CHARACTERS_IN_THIS_SERIES, totalNumberOfCharactersInSeries);
	}

	@Override
	public void setTotalNumberOfBadCharacters(int totalNumberOfBadCharacters) {
		setIntValue(TOTAL_NUMBER_OF_BAD_CHARACTERS, totalNumberOfBadCharacters);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * net.sf.marineapi.nmea.sentence.GLLSentence#setDataStatus(net.sf.marineapi
	 * .nmea.util.DataStatus)
	 */
	@Override
	public void setStatus(DataStatus status) {
		setCharValue(DATA_STATUS, status.toChar());
	}

	@Override
	public void setMessageBody(String messageBody) {
		setStringValue(MESSAGE_BODY, messageBody);
	}
}
