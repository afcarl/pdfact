package icecite.visualize;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfDocument;
import icecite.models.PdfElement;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfType;
import icecite.models.PdfWord;
import icecite.utils.collection.CollectionUtils;

/**
 * A plain implementation of PdfVisualizer.
 *
 * @author Claudius Korzen
 */
public class PlainPdfVisualizer implements PdfVisualizer {
  /**
   * The factory to create instance of {@link PdfDrawer}.
   */
  protected PdfDrawerFactory pdfDrawerFactory;

  /**
   * The set of types of the PDF elements to serialize.
   */
  protected Set<PdfType> types;

  /**
   * The set of roles of PDF elements to serialize.
   */
  protected Set<PdfRole> roles;

  /**
   * The random generator needed to create random colors.
   */
  protected Random r;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF visualizer.
   * 
   * @param pdfDrawerFactory
   *        The factory to create instances of {@link PdfDrawerFactory}.
   */
  @AssistedInject
  public PlainPdfVisualizer(PdfDrawerFactory pdfDrawerFactory) {
    this.pdfDrawerFactory = pdfDrawerFactory;
    this.types = new HashSet<>();
    this.roles = new HashSet<>();
    this.r = new Random();
  }

  /**
   * Creates a new PDF visualizer.
   * 
   * @param pdfDrawerFactory
   *        The factory to create instances of {@link PdfDrawerFactory}.
   * @param types
   *        The types of the PDF elements to visualize.
   */
  @AssistedInject
  public PlainPdfVisualizer(PdfDrawerFactory pdfDrawerFactory,
      @Assisted Set<PdfType> types) {
    this(pdfDrawerFactory);
    this.types = types;
    this.roles = new HashSet<>();
  }

  /**
   * Creates a new PDF visualizer.
   * 
   * @param pdfDrawerFactory
   *        The factory to create instances of {@link PdfDrawerFactory}.
   * @param types
   *        The type of the PDF elements to visualize.
   * @param roles
   *        The roles of the PDF elements to visualize.
   */
  @AssistedInject
  public PlainPdfVisualizer(PdfDrawerFactory pdfDrawerFactory,
      @Assisted("types") Set<PdfType> types,
      @Assisted("roles") Set<PdfRole> roles) {
    this(pdfDrawerFactory);
    this.types = types;
    this.roles = roles;
  }

  // ==========================================================================

  @Override
  public void visualize(PdfDocument pdf, OutputStream stream)
      throws IOException {
    PdfDrawer drawer = this.pdfDrawerFactory.create(pdf.getFile());

    for (PdfPage page : pdf.getPages()) {
      visualizePage(page, drawer);
    }

    drawer.writeTo(stream);
  }

  // ==========================================================================

  /**
   * Visualizes the given features of the given document using the given
   * drawer.
   * 
   * @param page
   *        The page to process.
   * @param drawer
   *        The drawer to use.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizePage(PdfPage page, PdfDrawer drawer)
      throws IOException {
    if (page == null) {
      return;
    }
    
    // Visualize the text elements.
    for (PdfTextBlock block : page.getTextBlocks()) {
//      if (isVisualizePdfElement(block)) {
//        visualizePdfElement(page, block, drawer, Color.RED);
//      }
      for (PdfTextLine line : block.getTextLines()) {
//        if (isVisualizePdfElement(line)) {
//          visualizePdfElement(page, line, drawer, Color.BLUE);
//        }
        for (PdfWord word : line.getWords()) {
//          if (isVisualizePdfElement(word)) {
//            visualizePdfElement(page, word, drawer, Color.ORANGE);
//          }
          for (PdfCharacter character : word.getCharacters()) {
            if (isVisualizePdfElement(character)) {
              visualizePdfElement(page, character, drawer, Color.BLUE);
            }
          }
        }
      }
    }
        
//    // Visualize the graphical elements.
//    for (PdfFigure figure : page.getFigures()) {
//      if (isVisualizePdfElement(figure)) {
//        visualizePdfElement(page, figure, drawer, Color.CYAN);
//      }
//    }
//    for (PdfShape shape : page.getShapes()) {
//      if (isVisualizePdfElement(shape)) {
//        visualizePdfElement(page, shape, drawer, Color.MAGENTA);
//      }
//    }
  }

  /**
   * Visualizes the given list of rectangles using the given drawer.
   * 
   * @param page
   *        The page in which the element is located.
   * @param element
   *        The element to visualize.
   * @param drawer
   *        The drawer to use.
   * @param strokingColor
   *        The color to use.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizePdfElement(PdfPage page, PdfElement element,
      PdfDrawer drawer, Color strokingColor) throws IOException {
    visualizePdfElement(page, element, drawer, strokingColor, null);
  }

  /**
   * Visualizes the given list of rectangles using the given drawer.
   * 
   * @param page
   *        The page in which the element is located.
   * @param element
   *        The element to visualize.
   * @param drawer
   *        The drawer to use.
   * @param strokingColor
   *        The color to use on stroking the edges of the rectangle.
   * @param nonStrokingColor
   *        The color to use on filling the rectangle.
   * @throws IOException
   *         If the drawing failed.
   */
  protected void visualizePdfElement(PdfPage page, PdfElement element,
      PdfDrawer drawer, Color strokingColor, Color nonStrokingColor)
      throws IOException {
    if (element != null) {
      drawer.drawBoundingBox(element, page.getPageNumber(), strokingColor,
          nonStrokingColor);
      if (element.getRole() != null) {
        // Visualize the role.
        drawer.drawText(element.getRole().getName(), page.getPageNumber(), 
            element.getRectangle().getUpperLeft(), strokingColor, 8);
      }
    }
  }

  // ==========================================================================

  /**
   * Checks if the given PDF element should be visualized or not, dependent on
   * the type and the role of the element.
   * 
   * @param element
   *        The PDF element to check.
   * @return True, if the given PDF element should be visalized, false
   *         otherwise.
   */
  protected boolean isVisualizePdfElement(PdfElement element) {
    // Check if the type of the given element was registered to be serialized.
    boolean isTypeGiven = !CollectionUtils.isNullOrEmpty(this.types);
    if (isTypeGiven && !this.types.contains(element.getType())) {
      return false;
    }

    // Check if the role of the given element was registered to be serialized.
    boolean isRoleGiven = !CollectionUtils.isNullOrEmpty(this.roles);
    if (isRoleGiven && !this.roles.contains(element.getRole())) {
      return false;
    }
    return true;
  }
}