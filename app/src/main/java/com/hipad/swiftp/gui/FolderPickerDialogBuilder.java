/*******************************************************************************
 * Copyright (c) 2012-2015 Pieter Pareit.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors:
 * Pieter Pareit - initial API and implementation
 ******************************************************************************/

package com.hipad.swiftp.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Builder class for a folder picker dialog.
 */
public class FolderPickerDialogBuilder extends AlertDialog.Builder {

    private ArrayAdapter<String> mAdapter;
    private AlertDialog mAlert;

    private File mRoot;

    public FolderPickerDialogBuilder(Context context, File root) {
        super(context);
        mRoot = root;

        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

        update();

        ListView list = new ListView(getContext());
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dir = (String) parent.getItemAtPosition(position);
                final File parentFile;
                if (dir.equals("..") && (parentFile = mRoot.getParentFile()) != null) {
                    mRoot = parentFile;
                } else {
                    mRoot = new File(mRoot, dir);
                }
                update();
            }
        });

        setView(list);
    }

    @Override
    public AlertDialog create() {
        if (mAlert != null) throw new RuntimeException("Cannot reuse builder");
        mAlert = super.create();
        return mAlert;
    }

    private void update() {
        try {
            mRoot = new File(mRoot.getCanonicalPath());
        } catch (IOException e) {
            mRoot = Environment.getExternalStorageDirectory();
        }

        if (mAlert != null) {
            mAlert.setTitle(mRoot.getAbsolutePath());
        } else {
            setTitle(mRoot.getAbsolutePath());
        }

        mAdapter.clear();
        String[] dirs = mRoot.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File file = new File(dir, name);
                return (file.isDirectory() && !file.isHidden());
            }
        });
        if (dirs == null) {
            dirs = new String[]{};
        }
        mAdapter.add("..");
        mAdapter.addAll(dirs);
    }

    public AlertDialog.Builder setSelectedButton(int textId, final OnSelectedListener listener) {
        return setPositiveButton(textId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onSelected(mRoot.getAbsolutePath());
            }
        });
    }

    public interface OnSelectedListener {
        void onSelected(String path);
    }

}
