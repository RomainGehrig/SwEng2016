package icynote.exporters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import icynote.note.Note;
import icynote.plugins.ImageFormatter;
import util.ArgumentChecker;

public class HTMLExporter implements NoteExporter<HTMLExporter.HTMLNote> {
    @Override
    public HTMLNote export(Note<SpannableString> note, Context context) {
        // For the moment we implement a quite dumb export
        // TODO: convert special characters to their html equivalent
        StringBuilder sb = new StringBuilder();
        String title = Html.escapeHtml(note.getTitle());
        SpannableString spanContent = note.getContent();

        ImageFormatter.ImageSpanWithId[] spans = spanContent.getSpans(
                0,
                spanContent.length(),
                ImageFormatter.ImageSpanWithId.class);

        SpannableString translatedContent = new SpannableString(spanContent);
        // Convert image URIs found as spans to Base64 to embed them in the HTML source
        for (ImageFormatter.ImageSpanWithId span : spans) {
            Uri img = Uri.parse(span.getName());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), img);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                byte[] bytes = out.toByteArray();
                String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);

                translatedContent.removeSpan(span);
                translatedContent.setSpan(new ImageSpan(span.getDrawable(), "data:image/png;base64," + encoded),
                        spanContent.getSpanStart(span),
                        spanContent.getSpanEnd(span),
                        spanContent.getSpanFlags(span));
            } catch (IOException e) {
                // We don't convert the image if it doesn't exist
                continue;
            }
        }

        String content = Html.toHtml(translatedContent);
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
