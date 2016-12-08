package icynote.exporters;

import android.text.SpannableString;
import android.util.Log;

import java.io.UnsupportedEncodingException;

import icynote.note.Note;
import util.ArgumentChecker;

public class HTMLExporter implements NoteExporter<HTMLExporter.HTMLNote> {
    @Override
    public HTMLNote export(Note<SpannableString> note) {
        // For the moment we implement a quite dumb export
        // TODO: convert special characters to their html equivalent
        StringBuilder sb = new StringBuilder();
        String title = note.getTitle().toString();
        String content = note.getContent().toString();
        content = content.replace("\n", "<br/>");
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("  <head>\n");
        sb.append("    <meta charset=\"utf-8\">\n");
        sb.append("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n");
        sb.append("    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n");
        sb.append("    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\">\n");
        sb.append("    <title>"); sb.append(title); sb.append("</title>\n");
        sb.append("  </head>\n");
        sb.append("  <body>\n");
        sb.append("    <div class=\"container\">\n");
        sb.append("      <div class=\"jumbotron\">\n");
        sb.append("        <h1>"); sb.append(title); sb.append("</h1>\n");
        sb.append("        <p>"); sb.append(content); sb.append("</p>\n");
        sb.append("      </div>\n");
        sb.append("    </div>\n");
        sb.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js\"></script>\n");
        sb.append("    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>\n");
        sb.append("  </body>\n");
        sb.append("</html>\n");
        return new HTMLNote(sb.toString());
    }

    public static class HTMLNote implements NoteExporter.ExportedNote {
        private final String htmlBody;

        public HTMLNote(String htmlBody) {
            this.htmlBody = ArgumentChecker.requireNonNull(htmlBody);
        }

        @Override
        public byte[] getBytes() {
            byte[] toWrite = null;

            try {
                toWrite = htmlBody.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e("HTMLExporter", "Failed to convert the body to utf-8, backing off to plain ASCII.", e);
                toWrite = htmlBody.getBytes();
            }
            return toWrite;
        }
    }
}
