/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.devtools.intellij.protoeditor.ide.documentation;

import com.google.devtools.intellij.protoeditor.fixtures.PbCodeInsightFixtureTestCase;
import com.google.devtools.intellij.protoeditor.lang.PbFileType;
import com.google.devtools.intellij.protoeditor.lang.psi.PbFile;
import com.google.devtools.intellij.protoeditor.lang.psi.PbStatement;
import com.intellij.psi.util.QualifiedName;
import java.util.Arrays;

/** Tests for {@link PbDocumentationProvider}. */
public class PbDocumentationProviderTest extends PbCodeInsightFixtureTestCase {

  public void testGenerateDocReturnsFormattedComments() throws Exception {
    PbStatement msg =
        loadSymbol(
            "Msg",
            "syntax = 'proto2';",
            "",
            "// Comments for Msg. Example:",
            "//   Msg.abcd = 5",
            "// <escaped-tag/>",
            "message Msg {",
            "}");
    String doc = new PbDocumentationProvider().generateDoc(msg, null);
    assertEquals(
        "<pre>Comments for Msg. Example:\n  Msg.abcd = 5\n&lt;escaped-tag/&gt;\n</pre>", doc);
  }

  public void testGenerateDocWithNoCommentsReturnsNull() throws Exception {
    PbStatement msg = loadSymbol("Msg", "syntax = 'proto2';", "", "message Msg {", "}");
    String doc = new PbDocumentationProvider().generateDoc(msg, null);
    assertNull(doc);
  }

  private PbStatement loadSymbol(String symbol, String... lines) {
    String text = String.join("\n", Arrays.asList(lines));
    PbFile file = (PbFile) myFixture.configureByText(PbFileType.INSTANCE, text);
    return file.findSymbols(QualifiedName.fromDottedString(symbol), PbStatement.class)
        .iterator()
        .next();
  }
}
