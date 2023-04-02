package com.semtleWebGroup.youtubeclone.domain.video_media.util;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpRange;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceRegionFactory {

    public static List<ResourceRegion> fromRanges(List<HttpRange> ranges, Resource resource, long maxChunkSize) throws IOException {
        List<ResourceRegion> regions = new ArrayList<>(ranges.size());
        for (HttpRange range : ranges) {
            regions.add(fromRange(range, resource,maxChunkSize));
        }
        if (ranges.size() > 1) {
            long length = resource.contentLength();
            long total = 0;
            for (ResourceRegion region : regions) {
                total += region.getCount();
            }
            if (total >= length) {
                throw new IllegalArgumentException("The sum of all ranges (" + total +
                        ") should be less than the resource length (" + length + ")");
            }
        }
        return regions;
    }

    public static ResourceRegion fromRange(HttpRange range, Resource resource, long maxChunkSize) throws IOException {
        Assert.isTrue(resource.getClass() != InputStreamResource.class,
                "Cannot convert an InputStreamResource to a ResourceRegion");

        //기본 정보 파싱
        long contentLength = resource.contentLength();
        long start = range.getRangeStart(contentLength);
        long end = range.getRangeEnd(contentLength);

        Assert.isTrue(start < contentLength, () -> "'position' exceeds the resource length " + contentLength);
        //max chunk 보다 큰지 체크
        if(end-start > maxChunkSize){
            end = start+maxChunkSize;
        }

        return new ResourceRegion(resource, start, end - start + 1);
    }
}
