/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */

package org.elasticsearch.xpack.runtimefields;

import org.apache.lucene.index.LeafReaderContext;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.script.AggregationScript;
import org.elasticsearch.search.lookup.DocLookup;
import org.elasticsearch.search.lookup.LeafDocLookup;
import org.elasticsearch.search.lookup.SourceLookup;

import java.util.Map;

/**
 * Abstract base for scripts to execute to build scripted fields. Inspired by
 * {@link AggregationScript} but hopefully with less historical baggage.
 */
public abstract class AbstractScriptFieldsScript {
    private final Map<String, Object> params;
    private final LeafReaderContext ctx;
    private final SourceLookup source;
    private final LeafDocLookup fieldData;

    public AbstractScriptFieldsScript(Map<String, Object> params, SourceLookup source, DocLookup fieldData, LeafReaderContext ctx) {
        this.params = params;
        this.source = source;
        this.fieldData = fieldData.getLeafDocLookup(ctx);
        this.ctx = ctx;
    }

    /**
     * Set the document to run the script against.
     */
    public void setDocument(int docId) {
        source.setSegmentAndDocument(ctx, docId);
        fieldData.setDocument(docId);
    }

    /**
     * Expose the {@code params} of the script to the script itself.
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * Expose the {@code _source} to the script.
     */
    public Map<String, Object> getSource() {
        return source;
    }

    /**
     * Expose field data to the script as {@code doc}.
     */
    public Map<String, ScriptDocValues<?>> getDoc() {
        return fieldData;
    }
}