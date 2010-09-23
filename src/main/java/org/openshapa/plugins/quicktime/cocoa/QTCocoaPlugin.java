package org.openshapa.plugins.quicktime.cocoa;

import java.io.FileFilter;

import java.net.URL;

import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;


import org.openshapa.views.continuous.DataViewer;
import org.openshapa.views.continuous.Filter;
import org.openshapa.views.continuous.FilterNames;
import org.openshapa.views.continuous.Plugin;

import com.google.common.collect.Lists;

import com.sun.jna.Platform;


public final class QTCocoaPlugin implements Plugin {

    private static final Filter VIDEO_FILTER = new Filter() {
            final SuffixFileFilter ff;
            final List<String> ext;

            {
                ext = Lists.newArrayList(".avi", ".mov", ".mpg", ".mp4");
                ff = new SuffixFileFilter(ext, IOCase.INSENSITIVE);
            }

            @Override public FileFilter getFileFilter() {
                return ff;
            }

            @Override public String getName() {
                return FilterNames.VIDEO.getFilterName();
            }

            @Override public Iterable<String> getExtensions() {
                return ext;
            }
        };

    @Override public DataViewer getNewDataViewer(final java.awt.Frame parent,
        final boolean modal) {

        if (Platform.isMac()) {
            return new QTKitVer7DataViewer(parent, modal);
        } else {
            return null;
        }
    }

    /**
     * @return icon representing this plugin.
     */
    @Override public ImageIcon getTypeIcon() {
        URL typeIconURL = getClass().getResource(
                "/icons/gstreamerplugin-icon.png");

        return new ImageIcon(typeIconURL);
    }

    @Override public String getClassifier() {
        return "openshapa.video";
    }

    @Override public Filter[] getFilters() {
        return new Filter[] { VIDEO_FILTER };
    }

    @Override public String getPluginName() {
        return "UNSTABLE: QTCocoa Video";
    }

}
