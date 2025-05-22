package com.gerefi.output;

import com.gerefi.ConfigField;
import com.gerefi.ReaderState;

import java.util.List;

/**
 * @see FieldsStrategy
 * @see FieldIterator is there a duplication?
 */
class PerFieldWithStructuresIterator extends FieldIterator {
    private final ReaderState state;
    private final String variableNamePrefix;
    private final Strategy strategy;
    private final String prefixSeparator;
    private final StringBuilder sb = new StringBuilder();

    public PerFieldWithStructuresIterator(ReaderState state, List<ConfigField> fields, String variableNamePrefix, Strategy strategy, String prefixSeparator) {
        super(fields);
        this.state = state;
        this.variableNamePrefix = variableNamePrefix;
        this.strategy = strategy;
        this.prefixSeparator = prefixSeparator;
    }

    public PerFieldWithStructuresIterator(ReaderState state, List<ConfigField> fields, String variableNamePrefix, Strategy strategy) {
        this(state, fields, variableNamePrefix, strategy, "_");
    }

    @Override
    public void end(int currentPosition) {
        ConfigStructure cs = cf.getState().getStructures().get(cf.getTypeName());
        String content;
        if (cs != null) {
            if (strategy.skip(cf)) {
                // do not support this case yet
                content = "";
            } else {
                // java side of things does not care for 'cs.withPrefix'
                String extraPrefix = variableNamePrefix + strategy.getArrayElementName(cf) + prefixSeparator;
                PerFieldWithStructuresIterator fieldIterator = new PerFieldWithStructuresIterator(state, cs.getTsFields(), extraPrefix, strategy, prefixSeparator);
                fieldIterator.loop(currentPosition);
                content = fieldIterator.sb.toString();
            }
        } else {
            content = strategy.process(state, cf, variableNamePrefix, currentPosition, this);
        }
        sb.append(content);
        super.end(currentPosition);
    }

    public String getContent() {
        return sb.toString();
    }

    interface Strategy {
        String process(ReaderState state, ConfigField configField, String prefix, int currentPosition, PerFieldWithStructuresIterator perFieldWithStructuresIterator);

        default String getArrayElementName(ConfigField cf) {
            return cf.getName();
        }

        default boolean skip(ConfigField cf) {
            return cf.isFromIterate();
        }
    }
}
