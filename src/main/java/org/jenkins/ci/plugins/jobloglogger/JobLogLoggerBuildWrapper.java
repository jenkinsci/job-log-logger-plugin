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

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Run.RunnerAbortedException;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.TeeOutputStream;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author <a href="mailto:jieryn@gmail.com">Jesse Farinacci</a>
 * @since 1.0
 */
public final class JobLogLoggerBuildWrapper extends BuildWrapper {
    /**
     * Plugin marker for BuildWrapper.
     */
    @Extension
    public static class DescriptorImpl extends BuildWrapperDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.JobLogLogger_DisplayName();
        }

        @Override
        public boolean isApplicable(final AbstractProject<?, ?> item) {
            return true;
        }
    }

    private static final boolean DEFAULT_SUPPRESS_EMPTY = true;

    /**
     * Whether or not to suppress empty job log messages from being logged to
     * the underlying logging system.
     */
    private boolean              suppressEmpty;

    public JobLogLoggerBuildWrapper() {
        this(DEFAULT_SUPPRESS_EMPTY);
    }

    @DataBoundConstructor
    public JobLogLoggerBuildWrapper(final boolean suppressEmpty) {
        super();
        this.suppressEmpty = suppressEmpty;
    }

    @Override
    public OutputStream decorateLogger(
            @SuppressWarnings("rawtypes") final AbstractBuild build,
            final OutputStream logger) throws IOException,
            InterruptedException, RunnerAbortedException {
        return new TeeOutputStream(logger, new AbstractBuildJULOutputStream(
                build));
    }

    public boolean isSuppressEmpty() {
        return suppressEmpty;
    }

    public void setSuppressEmpty(final boolean suppressEmpty) {
        this.suppressEmpty = suppressEmpty;
    }

    @Override
    public Environment setUp(
            @SuppressWarnings("rawtypes") final AbstractBuild build,
            final Launcher launcher, final BuildListener listener)
            throws IOException, InterruptedException {
        return new Environment() {
            /* empty implementation */
        };
    }
}
