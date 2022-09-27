package com.tuannq.authentication.model.type;

import java.util.regex.Pattern;

public class RegexType {
    /**
     *   ^[a-zA-Z0-9]      # start with an alphanumeric character
     *   (                 # start of (group 1)
     *     [._-](?![._-])  # follow by a dot, hyphen, or underscore, negative lookahead to
     *                     # ensures dot, hyphen, and underscore does not appear consecutively
     *     |               # or
     *     [a-zA-Z0-9]     # an alphanumeric character
     *   )                 # end of (group 1)
     *   {3,18}            # ensures the length of (group 1) between 3 and 18
     *   [a-zA-Z0-9]$      # end with an alphanumeric character
     * <p>
     *                     # {3,18} plus the first and last alphanumeric characters,
     *                     # total length became {5,20}
     */
    private static final String USERNAME_PATTERN =
            "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
    public static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public static final Pattern HEX_WEBCOLOR_PATTERN =
            Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");

    public static final Pattern DATE_PATTERN =
            Pattern.compile("^(?:(?:31(\\/|-)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");

    /**
     * ^                                   # start of line
     * (?=.*[0-9])                       # positive lookahead, digit [0-9]
     * (?=.*[a-z])                       # positive lookahead, one lowercase character [a-z]
     * (?=.*[A-Z])                       # positive lookahead, one uppercase character [A-Z]
     * (?=.*[!@#&()–[{}]:;,?/*~$^+=<>.]) # positive lookahead, one of the special character in this [..]
     * .                                 # matches anything
     * {6,32}                            # length at least 6 characters and maximum of 32 characters
     * $
     */
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;,?/*~$^+=<>.]).{6,32}$");


    public static final Pattern PHONE_PATTERN = Pattern.compile("^0([1-9])([0-9]{8})$");

    public static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

    public static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    public static final Pattern WHITESPACE = Pattern.compile("[\\s]");
}
