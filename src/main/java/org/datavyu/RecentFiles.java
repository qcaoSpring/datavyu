/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.datavyu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.application.LocalStorage;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Remembers, saves, and loads lists of recent files for Datavyu's history
 * functionality in the menus.
 */
public enum RecentFiles {

    INSTANCE;

    /**
     * Max files per list to remember.
     */
    private static final int HISTORY_LIMIT = 5;

    /**
     * Name of the history file to load from.
     */
    private static final String fileName = "recent_files.yml";

    /**
     * Class logger.
     */
    private static Logger LOGGER = LogManager.getLogger(RecentFiles.class);
    /**
     * The history file to read and write to.
     */
    private final File historyFile;
    /**
     * List of recently opened projects.
     */
    private List<File> projects;
    /**
     * List of recently opened scripts.
     */
    private List<File> scripts;

    private RecentFiles() {
        LocalStorage storage = Datavyu.getApplication().getContext()
                .getLocalStorage();

        projects = new LinkedList<File>();
        scripts = new LinkedList<File>();

        historyFile = new File(storage.getDirectory(), fileName);

        if (historyFile.exists()) {
            load();
        }
    }

    /**
     * Remember the given file in the list of projects.
     */
    public static void rememberProject(final File file) {
        remember(INSTANCE.projects, file);
        INSTANCE.save();
    }

    /**
     * Remember the given file in the list of scripts.
     */
    public static void rememberScript(final File file) {
        remember(INSTANCE.scripts, file);
        INSTANCE.save();
    }

    private static void remember(final List<File> history, final File file) {

        if (history.contains(file)) {
            history.remove(file);
        }

        if (history.size() == HISTORY_LIMIT) {
            history.remove(HISTORY_LIMIT - 1);
        }

        history.add(0, file);

    }

    /**
     * Recently opened projects, most recent first.
     */
    public static Iterable<File> getRecentProjects() {
        return INSTANCE.projects;
    }

    /**
     * Recently opened scripts, most recent first.
     */
    public static Iterable<File> getRecentScripts() {
        return INSTANCE.scripts;
    }

    /**
     * Save history to disk in YAML format.
     */
    private void save() {
        Map<String, List<String>> historyMap = Maps.newHashMap();
        historyMap.put("projects", filesToPaths(projects));
        historyMap.put("scripts", filesToPaths(scripts));

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);
        Writer fw = null;

        try {
            fw = new FileWriter(historyFile);
            yaml.dump(historyMap, fw);
        } catch (IOException e) {
            LOGGER.error("Couldn't save history", e);
        } finally {
            IOUtils.closeQuietly(fw);
        }
    }


    /**
     * Load history from disk.
     */
    private void load() {
        Yaml yaml = new Yaml();
        Reader fr = null;

        try {
            fr = new FileReader(historyFile);

            Map data = (Map) yaml.load(fr);

            List<String> projectPaths = (List) data.get("projects");
            projects = pathsToFiles(projectPaths);

            List<String> scriptPaths = (List) data.get("scripts");
            scripts = pathsToFiles(scriptPaths);

        } catch (FileNotFoundException e) {
            ; // Function is only called if the file exists.
        } finally {
            IOUtils.closeQuietly(fr);
        }
    }

    private List<String> filesToPaths(final List<File> files) {
        List<String> paths = Lists.newLinkedList();

        for (File f : files) {
            paths.add(f.getAbsolutePath());
        }

        return paths;
    }

    private List<File> pathsToFiles(final List<String> paths) {
        List<File> files = Lists.newLinkedList();

        for (String path : paths) {
            files.add(new File(path));
        }

        return files;
    }

}
