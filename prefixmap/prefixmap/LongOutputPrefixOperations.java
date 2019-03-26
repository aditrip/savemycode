package com.sleepycat.util.prefixmap;

import java.io.IOException;

public class LongOutputPrefixOperations extends OutputPrefixOperations<Long> {

    /**
     * Needs to be singleton so that operators "==" and "!=" work.
     */
    private final static Long NULL_OUTPUT = new Long(0L);

    private final static Long NO_OUTPUT = new Long(-2L);

    private final static LongOutputPrefixOperations singleton =
        new LongOutputPrefixOperations();

    private LongOutputPrefixOperations() {
    }

    public static LongOutputPrefixOperations getInstance() {
        return singleton;
    }

    @Override
    public Long commonPrefix(Long e1, Long e2) {
        if (e1 == NULL_OUTPUT || e2 == NULL_OUTPUT) {
            return NULL_OUTPUT;
        }
        if (e1 < 0 || e2 < 0) {
            throw new IllegalArgumentException("LongOutputPrefixOperations" +
                    " is valid only for positive integers");
        }
        return Math.min(e1, e2);

    }

    @Override
    public Long substract(Long e1, Long e2) {
        if (e1 == null) {
            e1 = NULL_OUTPUT;
        }
        if (e2 == null) {
            e2 = NULL_OUTPUT;
        }
        if (e1 < 0 || e2 < 0) {
            throw new IllegalArgumentException("LongOutputPrefixOperations" +
                    " is valid only for positive integers");
        }
        if (e2 > e1) {
            throw new IllegalArgumentException(e2 + ">" + e1);
        }

        if (e2 == NULL_OUTPUT) {
            return e1;
        } else if (e1.equals(e2)) {
            return NULL_OUTPUT;
        } else {
            return e1 - e2;
        }
    }

    @Override
    public Long add(Long prefix, Long e2) {
        if (prefix == null) {
            prefix = NULL_OUTPUT;
        }
        if (e2 == null) {
            e2 = NULL_OUTPUT;
        }
        if (prefix == NULL_OUTPUT) {
            return e2;
        } else if (e2 == NULL_OUTPUT) {
            return prefix;
        } else {
            return prefix + e2;
        }
    }

    @Override
    public Long getNullOutput() {
        return NULL_OUTPUT;
    }

    @Override
    public Long getNoOutput() {
        return NO_OUTPUT;
    }

    @Override
    public void write(Long output, PrefixMapWriter writer) throws IOException {
        if (output > 0 || output == NULL_OUTPUT) {
            writer.writeVarLong(output);
        }
    }

    @Override
    public void writeReverse(Long output, PrefixMapWriter writer)
        throws IOException {
        if (output > 0 || output == NULL_OUTPUT) {
            writer.writeVarLongReverse(output);
        }

    }

    @Override
    public Long read(PrefixMapReader reader) throws IOException {

        long l = reader.readVarLong();
        if (l == 0) {
            return NULL_OUTPUT;
        }
        return l;

    }

}
