export type BucketFile = {
    id?: string;
    nome?: string;
    fileId?: string,
    size?: number,
    contentType?: string,
    inputStream?: Blob,
    multipartFile?: string
}

export type EntityWithBucketFile = {
    id?: string,
    bucketFile?: BucketFile
};