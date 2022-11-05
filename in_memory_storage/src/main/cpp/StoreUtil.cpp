//
// Created by Ihor Kucherenko on 6/22/17.
//
#include "StoreUtil.h"
#include "Store.h"
#include <cstdlib>
#include <cstring>
#include <android/log.h>

bool isEntryValid(JNIEnv* pEnv, StoreEntry* pEntry, StoreType pType)
{
    if(pEntry == nullptr)
    {
        throwNoKeyException(pEnv);
    }

    return ((pEntry != nullptr) && (pEntry->mType == pType));
}

StoreEntry* findEntry(JNIEnv* pEnv, Store* pStore, jstring pKey)
{
    StoreEntry* entry = pStore->mEntries;
    StoreEntry* endEntry = entry + pStore->mLength;
    const char* tmpKey = pEnv->GetStringUTFChars(pKey, nullptr);
    while ((entry < endEntry) && (strcmp(entry->mKey, tmpKey) != 0)) {
        ++entry;
    }
    pEnv->ReleaseStringUTFChars(pKey, tmpKey);
    return (entry == endEntry) ? nullptr : entry;
}

StoreEntry* allocateEntry(JNIEnv* pEnv, Store* pStore, jstring pKey)
{
    StoreEntry* entry = findEntry(pEnv, pStore, pKey);
    if( entry !=  nullptr) {
        releaseEntryValue(pEnv, entry);
    } else {
        entry = pStore->mEntries + pStore->mLength;

        const char* tmpKey = pEnv->GetStringUTFChars(pKey, nullptr);
        entry->mKey = new char[strlen(tmpKey) + 1];
        strcpy(entry->mKey, tmpKey);
        pEnv->ReleaseStringUTFChars(pKey, tmpKey);

        ++pStore->mLength;
    }
    return entry;
}

void releaseEntryValue(JNIEnv* pEnv, StoreEntry* pEntry)
{
    switch (pEntry->mType) {
        case StoreType_String:
            delete pEntry->mValue.mString;
            break;
        case StoreType_Object:
            pEnv->DeleteGlobalRef(pEntry->mValue.mObject);
            break;
        default: ;
    }
}

void throwNoKeyException(JNIEnv* pEnv) {
    jclass clazz = pEnv->FindClass("java/lang/IllegalArgumentException");
    if (clazz != nullptr) {
        pEnv->ThrowNew(clazz, "Key does not exist.");
    }
    pEnv->DeleteLocalRef(clazz);
}