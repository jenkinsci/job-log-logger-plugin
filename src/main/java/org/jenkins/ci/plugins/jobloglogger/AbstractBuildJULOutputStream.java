/*
 * The MIT License
 * 
 * Copyright (c) 2012, Jesse Farinacci
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkins.ci.plugins.jobloglogger;

import hudson.model.AbstractBuild;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

/**
 * An {@link java.io.OutputStream} implementation which actually just writes to
 * a {@link java.util.logger.Logger}, and where messages are prefixed by the
 * specified {@link hudson.model.AbstractBuild#toString()}.
 * 
 * TODO: it would probably be nice if we used per-
 * {@link hudson.model.AbstractBuild} loggers so that the user could control
 * output destinations based on the {@link hudson.model.AbstractBuild#getName}
 * -- i'm just not sure what kind of string escaping we'd need to do to be
 * legitimate JUL log name..
 * 
 * @author <a href="mailto:jieryn@gmail.com">Jesse Farinacci</a>
 * @since 1.0
 */
public final class AbstractBuildJULOutputStream extends OutputStream {
    private static final Logger  LOG                    = Logger.getLogger(AbstractBuildJULOutputStream.class
                                                                .getName());

    private static final boolean DEFAULT_SUPPRESS_EMPTY = true;

    private final String         prefix;

    private final boolean        suppressEmpty;

    public AbstractBuildJULOutputStream(
            @SuppressWarnings("rawtypes") final AbstractBuild build) {
        this(build, DEFAULT_SUPPRESS_EMPTY);
    }

    public AbstractBuildJULOutputStream(
            @SuppressWarnings("rawtypes") final AbstractBuild build,
            final boolean suppressEmpty) {
        super();
        prefix = build.toString() + ": ";
        this.suppressEmpty = suppressEmpty;
    }

    @Override
    public void write(final int b) throws IOException {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.info(prefix + (byte) b);
        }
    }

    @Override
    public void write(final byte[] b) throws IOException {
        if (LOG.isLoggable(Level.INFO)) {
            final String str = StringUtils.trim(new String(b));
            if (!suppressEmpty || StringUtils.isNotEmpty(str)) {
                LOG.info(prefix + str);
            }
        }
    }

    @Override
    public void write(final byte[] b, final int off, final int len)
            throws IOException {
        if (LOG.isLoggable(Level.INFO)) {
            final String str = StringUtils.trim(new String(b, off, len));
            if (!suppressEmpty || StringUtils.isNotEmpty(str)) {
                LOG.info(prefix + str);
            }
        }
    }
}
