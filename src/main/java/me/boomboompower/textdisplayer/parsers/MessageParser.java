/*
 *     Copyright (C) 2017 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.boomboompower.textdisplayer.parsers;

import me.boomboompower.textdisplayer.parsers.normal.*;

import java.util.HashMap;

/**
 * Created to replace the old Placeholder class
 *      due to it's inefficiency
 *
 * @author boomboompower
 */
public abstract class MessageParser {

    /* ParserName | MessageParser */
    private static final HashMap<String, MessageParser> parsers = new HashMap<>();

    /**
     * Default constructor for MessageParser
     */
    public MessageParser() {
    }

    /**
     * Run through all parsers and replace text
     *
     * @param input text to parse
     * @return the formatted text
     */
    public static String parseAll(String input) {
        for (MessageParser parser : parsers.values()) {
            input = parser.parse(new ParsedMessage(input)).getMessage();
        }
        return input;
    }

    /**
     * Adds the developers own MessageParser
     *
     * @param parser the developers parser
     */
    public static void addParser(MessageParser parser) {
        if (parser != null && parser.getName() != null) parsers.put(parser.getName(), parser);
    }

    public static void remake() {
        parsers.clear();
        parsers.put("MainParser", new MainParser());
        parsers.put("ItemParser", new ItemParser());
        parsers.put("DirectionParser", new DirectionParser());
        parsers.put("CPSParser", new CPSParser());
        parsers.put("FightingParser", new FightingParser());
    }

    public static MessageParser getParser(String name) {
        return parsers.getOrDefault(name, null);
    }

    /**
     * Returns the name of the specified MessageParser
     *
     * @return the name of the specified MessageParser, cannot be null
     */
    public abstract String getName();

    /**
     * Parses the given text and formats it
     *
     * @param parsedMessage message to format
     * @return the formatted message
     */
    public abstract ParsedMessage parse(final ParsedMessage parsedMessage);
}
