// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.firestore.core;

import static com.google.firebase.firestore.util.Assert.hardAssert;

import com.google.firebase.firestore.model.Document;
import com.google.firebase.firestore.model.DocumentKey;
import com.google.firebase.firestore.model.FieldPath;
import com.google.firebase.firestore.model.Values;
import com.google.firestore.v1.Value;
import java.util.ArrayList;
import java.util.List;

public class KeyFieldInFilter extends FieldFilter {
  private final List<DocumentKey> keys = new ArrayList<>();

  KeyFieldInFilter(FieldPath field, Value value) {
    super(field, Operator.IN, value);

    hardAssert(Values.isArray(value), "KeyFieldInFilter expects an ArrayValue");
    for (Value element : value.getArrayValue().getValuesList()) {
      hardAssert(
          Values.isReferenceValue(element),
          "Comparing on key with IN, but an array value was not a ReferenceValue");
      keys.add(DocumentKey.fromName(element.getReferenceValue()));
    }
  }

  @Override
  public boolean matches(Document doc) {
    return keys.contains(doc.getKey());
  }
}
