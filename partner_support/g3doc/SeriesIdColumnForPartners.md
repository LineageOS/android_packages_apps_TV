# 3rd party instructions for using series recording feature of Live Channels

## Prerequisites

*   Updated agreement with Google
*   Oreo or patched Nougat

## Nougat

To enable series recording with Nougat you will need the following changes.

### Patch TVProvider

To run in Nougat you must get ALL of the changes up to and including [this version]
(https://partner-android.googlesource.com/platform/packages/providers/TvProvider/+/079e905399549351fda402a21d15696a85211f6e)
of TV Provider (version 34 or above).

Indicate the SDK version of N is used by adding the following line to AndroidManifest.xml

Change "24" to "25" if it's N MR1 (7.1 or 7.1.1)

```
<uses-sdk android:minSdkVersion="24" android:targetSdkVersion="24"/>
```

### Customisation

Indicate TvProvider is patched by including the following in their TV
customization resource

```
<bool name="tvprovider_allows_column_creation">true</bool>
```

See https://source.android.com/devices/tv/customize-tv-app
