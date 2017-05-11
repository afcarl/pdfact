package icecite.models.plain;

import java.util.List;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfParagraph;
import icecite.models.PdfTextLine;

/**
 * A plain implementation of {@link PdfParagraph}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraph extends PlainPdfElement
    implements PdfParagraph {  
  /**
   * The characters of this page.
   */
  protected PdfCharacterSet characters;
  
  /**
   * The text lines of this paragraph.
   */
  protected List<PdfTextLine> textLines;

  /**
   * The text of this paragraph.
   */
  protected String text;

  // ==========================================================================

  /**
   * Creates a new paragraph.
   * 
   * @param textLines
   *        The text lines of this paragraph.
   */
  @AssistedInject
  public PlainPdfParagraph(@Assisted List<PdfTextLine> textLines) {
    this.textLines = textLines;
  }
  
  // ==========================================================================
  
  @Override
  public PdfCharacterSet getCharacters() {
    return this.characters;
  }

  @Override
  public void setCharacters(PdfCharacterSet characters) {
    this.characters = characters;
  }
  
  // ==========================================================================

  @Override
  public List<PdfTextLine> getTextLines() {
    return this.textLines;
  }

  @Override
  public void setTextLines(List<PdfTextLine> textLines) {
    this.textLines = textLines;
  }

  @Override
  public void addTextLine(PdfTextLine textLine) {
    this.textLines.add(textLine);
  }

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }
}